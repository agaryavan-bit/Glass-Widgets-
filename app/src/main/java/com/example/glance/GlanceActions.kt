package com.example.glance

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.action.ActionCallback
import com.example.WidgetConfigActivity
import com.example.data.DeviceTelemetryProvider
import com.example.data.WidgetConfigStore
import kotlin.random.Random

val PARAM_WIDGET_ID = ActionParameters.Key<Int>("widget_id")
val PARAM_SLOT_ID = ActionParameters.Key<String>("slot_id")
val PARAM_ACTION_TYPE = ActionParameters.Key<String>("action_type")
val PARAM_PAYLOAD = ActionParameters.Key<String>("payload")
val PARAM_INDEX = ActionParameters.Key<Int>("index")

/**
 * Interactive Glance ActionCallback powering tap-to-toggle checklists, water counter,
 * habit streak, quote cycling, dice/coin/tap/tic-tac-toe games, calculator keypad,
 * Pomodoro toggle, and music transport — 100% offline-safe!
 */
class InteractiveWidgetAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val manager = GlanceAppWidgetManager(context)
        val appWidgetId = parameters[PARAM_WIDGET_ID] ?: runCatching { manager.getAppWidgetId(glanceId) }.getOrDefault(-1)
        val actionType = parameters[PARAM_ACTION_TYPE].orEmpty()
        val payload = parameters[PARAM_PAYLOAD].orEmpty()
        val index = parameters[PARAM_INDEX] ?: 0
        val slotId = parameters[PARAM_SLOT_ID] ?: "2x2"

        if (appWidgetId < 0) return

        WidgetConfigStore.updateInteractiveState(context, appWidgetId) { cur ->
            when (actionType) {
                "toggle_check" -> cur.copy(checklistMask = cur.checklistMask xor (1 shl index))
                "water_inc" -> cur.copy(waterCount = if (cur.waterCount >= 12) 0 else cur.waterCount + 1)
                "habit_inc" -> cur.copy(habitStreak = cur.habitStreak + 1)
                "quote_next" -> cur.copy(quoteIndex = (cur.quoteIndex + 1) % DeviceTelemetryProvider.curatedQuotes.size)
                "dice_roll" -> cur.copy(diceValue = Random.nextInt(1, 7))
                "coin_flip" -> cur.copy(coinHeads = Random.nextBoolean())
                "tap_inc" -> cur.copy(tapCounter = cur.tapCounter + 1)
                "ttt_tap" -> {
                    val chars = cur.tttBoard.padEnd(9, '_').toCharArray()
                    val pos = index.coerceIn(0, 8)
                    if (chars[pos] == '_') {
                        val xCount = chars.count { it == 'X' }
                        val oCount = chars.count { it == 'O' }
                        chars[pos] = if (xCount <= oCount) 'X' else 'O'
                    } else if (chars.none { it == '_' }) {
                        for (i in 0 until 9) chars[i] = '_'
                    }
                    cur.copy(tttBoard = String(chars))
                }
                "ttt_reset" -> cur.copy(tttBoard = "_________")
                "pomo_toggle" -> cur.copy(
                    pomodoroRunning = !cur.pomodoroRunning,
                    pomodoroMinutes = if (cur.pomodoroMinutes <= 1) 25 else if (cur.pomodoroRunning) cur.pomodoroMinutes else (cur.pomodoroMinutes - 1).coerceAtLeast(1)
                )
                "music_toggle" -> cur.copy(musicPlaying = !cur.musicPlaying)
                "music_next" -> cur.copy(musicTrackIndex = (cur.musicTrackIndex + 1) % DeviceTelemetryProvider.curatedTracks.size)
                "music_prev" -> cur.copy(
                    musicTrackIndex = (cur.musicTrackIndex - 1 + DeviceTelemetryProvider.curatedTracks.size) % DeviceTelemetryProvider.curatedTracks.size
                )
                "calc_key" -> handleCalculatorKey(cur, payload)
                else -> cur
            }
        }

        GlassWidgetSlots.widgetForSlotId(slotId).update(context, glanceId)
    }

    private fun handleCalculatorKey(
        cur: com.example.model.WidgetInteractiveState,
        key: String
    ): com.example.model.WidgetInteractiveState {
        return when (key) {
            "C" -> cur.copy(calcDisplay = "0", calcPendingOp = "", calcAccumulator = 0.0)
            "+", "-", "×", "÷" -> {
                val value = cur.calcDisplay.toDoubleOrNull() ?: 0.0
                cur.copy(calcAccumulator = value, calcPendingOp = key, calcDisplay = "0")
            }
            "=" -> {
                val rhs = cur.calcDisplay.toDoubleOrNull() ?: 0.0
                val res = when (cur.calcPendingOp) {
                    "+" -> cur.calcAccumulator + rhs
                    "-" -> cur.calcAccumulator - rhs
                    "×" -> cur.calcAccumulator * rhs
                    "÷" -> if (rhs == 0.0) 0.0 else cur.calcAccumulator / rhs
                    else -> rhs
                }
                val formatted = if (res % 1.0 == 0.0) res.toLong().toString() else String.format("%.2f", res)
                cur.copy(calcDisplay = formatted.take(10), calcPendingOp = "", calcAccumulator = res)
            }
            else -> {
                val next = if (cur.calcDisplay == "0") key else (cur.calcDisplay + key).take(9)
                cur.copy(calcDisplay = next)
            }
        }
    }
}

/**
 * Launches system settings, camera intents, apps, web AI hubs, dialer, or the widget customizer.
 */
class LaunchShortcutAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val type = parameters[PARAM_ACTION_TYPE].orEmpty()
        val payload = parameters[PARAM_PAYLOAD].orEmpty()
        val widgetId = parameters[PARAM_WIDGET_ID] ?: -1

        val intent: Intent? = when (type) {
            "config" -> Intent(context, WidgetConfigActivity::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            }
            "settings_wifi" -> Intent(Settings.ACTION_WIFI_SETTINGS)
            "settings_bt" -> Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
            "settings_sound" -> Intent(Settings.ACTION_SOUND_SETTINGS)
            "settings_display" -> Intent(Settings.ACTION_DISPLAY_SETTINGS)
            "settings_airplane" -> Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
            "camera_photo" -> Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)
            "camera_video" -> Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA)
            "dial" -> Intent(Intent.ACTION_DIAL, Uri.parse("tel:$payload"))
            "sms" -> Intent(Intent.ACTION_VIEW, Uri.parse("sms:$payload"))
            "url" -> Intent(Intent.ACTION_VIEW, Uri.parse(payload))
            "app" -> {
                context.packageManager.getLaunchIntentForPackage(payload)
                    ?: Intent(Settings.ACTION_SETTINGS)
            }
            else -> context.packageManager.getLaunchIntentForPackage(context.packageName)
        }

        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            runCatching { context.startActivity(intent) }
        }
    }
}
