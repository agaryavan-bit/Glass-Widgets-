package com.example.feedback

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * Synthesizes a light, soothing ambient "Liquid Glass" harmonic pad & gentle chime loop
 * while the app is open in the foreground. Automatically stops and releases audio hardware
 * the instant all app activities go to background or the app is closed.
 */
class AmbientGlassAudioEngine {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var playbackJob: Job? = null

    private val _isSoundEnabled = MutableStateFlow(true)
    val isSoundEnabled: StateFlow<Boolean> = _isSoundEnabled.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    @Volatile
    private var isForeground: Boolean = false

    fun onAppForegrounded() {
        isForeground = true
        if (_isSoundEnabled.value) {
            startPlaybackLocked()
        }
    }

    fun onAppBackgrounded() {
        isForeground = false
        stopPlaybackLocked()
    }

    fun setSoundEnabled(enabled: Boolean) {
        _isSoundEnabled.value = enabled
        if (enabled && isForeground) {
            startPlaybackLocked()
        } else {
            stopPlaybackLocked()
        }
    }

    fun toggleSound(): Boolean {
        val next = !_isSoundEnabled.value
        setSoundEnabled(next)
        return next
    }

    @Synchronized
    private fun startPlaybackLocked() {
        if (playbackJob?.isActive == true) return
        _isPlaying.value = true
        playbackJob = scope.launch {
            runAmbientSynthLoop()
        }
    }

    @Synchronized
    private fun stopPlaybackLocked() {
        _isPlaying.value = false
        playbackJob?.cancel()
        playbackJob = null
    }

    private suspend fun runAmbientSynthLoop() {
        val sampleRate = 22050
        val minBuf = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        ).coerceAtLeast(2048)

        var track: AudioTrack? = null
        try {
            track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(minBuf * 2)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            if (track.state != AudioTrack.STATE_INITIALIZED) {
                track.release()
                return
            }

            track.setVolume(0.22f)
            track.play()

            // Warm Cmaj9 -> Am9 -> Fmaj7 -> G6 liquid-glass ambient chord progression (Hz)
            val chords = arrayOf(
                doubleArrayOf(261.63, 329.63, 392.00, 493.88, 587.33),
                doubleArrayOf(220.00, 261.63, 329.63, 392.00, 493.88),
                doubleArrayOf(174.61, 220.00, 261.63, 329.63, 392.00),
                doubleArrayOf(196.00, 246.94, 293.66, 329.63, 440.00)
            )
            // Soft crystalline shimmer bell frequencies
            val chimes = doubleArrayOf(1046.50, 1174.66, 1318.51, 1567.98, 1760.00)

            val chunkFrames = 1024
            val buffer = ShortArray(chunkFrames)
            var sampleIndex = 0L
            val chordDurationSec = 6.0
            val chimeIntervalSec = 3.0

            while (currentCoroutineContext().isActive && isForeground && _isSoundEnabled.value) {
                for (i in 0 until chunkFrames) {
                    val t = sampleIndex.toDouble() / sampleRate.toDouble()

                    // Gentle 1.5s fade-in on launch
                    val fadeIn = (t / 1.5).coerceIn(0.0, 1.0)

                    // Determine chord & smooth crossfade envelope
                    val chordPos = (t / chordDurationSec)
                    val chordIdx = chordPos.toInt() % chords.size
                    val nextChordIdx = (chordIdx + 1) % chords.size
                    val chordFrac = chordPos - chordPos.toInt()
                    val blend = if (chordFrac > 0.80) (chordFrac - 0.80) / 0.20 else 0.0

                    val curChord = chords[chordIdx]
                    val nextChord = chords[nextChordIdx]

                    // Slow LFO breathing modulation
                    val breath = 0.65 + 0.35 * sin(2.0 * PI * 0.14 * t)

                    var padSample = 0.0
                    for (n in curChord.indices) {
                        val s1 = sin(2.0 * PI * curChord[n] * t)
                        val s2 = sin(2.0 * PI * nextChord[n] * t)
                        val weight = if (n == 0) 0.28 else 0.18
                        padSample += ((1.0 - blend) * s1 + blend * s2) * weight
                    }
                    padSample *= breath * 0.38

                    // Delicate glass chime droplet every 3 seconds
                    val chimeSlot = (t / chimeIntervalSec).toInt()
                    val chimeLocalT = t - (chimeSlot * chimeIntervalSec)
                    val chimeFreq = chimes[chimeSlot % chimes.size]
                    val chimeEnv = exp(-chimeLocalT * 2.6) * (chimeLocalT * 18.0).coerceAtMost(1.0)
                    val chimeSample = (
                        sin(2.0 * PI * chimeFreq * t) * 0.7 +
                            sin(2.0 * PI * chimeFreq * 2.0 * t) * 0.15
                        ) * chimeEnv * 0.16

                    val mixed = (padSample + chimeSample) * fadeIn
                    val clamped = (mixed * 16000.0).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                    buffer[i] = clamped.toShort()
                    sampleIndex++
                }
                val written = track.write(buffer, 0, buffer.size)
                if (written < 0) break
            }
        } catch (_: Throwable) {
            // Gracefully ignore audio failures on headless/test environments (e.g. Robolectric)
        } finally {
            _isPlaying.value = false
            try {
                track?.pause()
                track?.flush()
                track?.stop()
                track?.release()
            } catch (_: Throwable) {
                // Safe cleanup
            }
        }
    }
}
