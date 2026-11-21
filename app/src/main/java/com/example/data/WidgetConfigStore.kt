package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.GlassTheme
import com.example.model.WidgetCategory
import com.example.model.WidgetInstanceConfig
import com.example.model.WidgetInteractiveState
import com.example.model.WidgetPresetRegistry
import com.example.model.WidgetSlotSize
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.glassWidgetDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "glass_widgets_store"
)

/**
 * Offline-safe Jetpack DataStore repository storing per-appWidgetId configurations
 * and interactive widget state.
 */
object WidgetConfigStore {

    private val KEY_AMBIENT_SOUND = booleanPreferencesKey("app_ambient_sound_enabled")
    private val KEY_PENDING_PIN_PRESET = stringPreferencesKey("pending_pin_preset_id")
    private val KEY_PENDING_PIN_THEME = stringPreferencesKey("pending_pin_theme")
    private val KEY_PENDING_PIN_OPACITY = floatPreferencesKey("pending_pin_opacity")
    private val KEY_PENDING_PIN_RADIUS = intPreferencesKey("pending_pin_radius")
    private val KEY_PENDING_PIN_ACCENT = longPreferencesKey("pending_pin_accent")
    private val KEY_PENDING_PIN_TEXT_COLOR = longPreferencesKey("pending_pin_text_color")
    private val KEY_PENDING_PIN_CUSTOM_TEXT = stringPreferencesKey("pending_pin_custom_text")
    private val KEY_PENDING_PIN_TARGET_DATE = longPreferencesKey("pending_pin_target_date")
    private val KEY_PENDING_PIN_APPS = stringPreferencesKey("pending_pin_apps")
    private val KEY_PENDING_PIN_PHOTO_URI = stringPreferencesKey("pending_pin_photo_uri")

    private fun presetKey(id: Int) = stringPreferencesKey("w_${id}_preset_id")
    private fun categoryKey(id: Int) = stringPreferencesKey("w_${id}_category")
    private fun themeKey(id: Int) = stringPreferencesKey("w_${id}_theme")
    private fun opacityKey(id: Int) = floatPreferencesKey("w_${id}_opacity")
    private fun radiusKey(id: Int) = intPreferencesKey("w_${id}_radius")
    private fun accentKey(id: Int) = longPreferencesKey("w_${id}_accent")
    private fun textColorKey(id: Int) = longPreferencesKey("w_${id}_text_color")
    private fun customTextKey(id: Int) = stringPreferencesKey("w_${id}_custom_text")
    private fun targetDateKey(id: Int) = longPreferencesKey("w_${id}_target_date")
    private fun selectedAppsKey(id: Int) = stringPreferencesKey("w_${id}_selected_apps")
    private fun photoUriKey(id: Int) = stringPreferencesKey("w_${id}_photo_uri")

    // Interactive state keys per appWidgetId
    private fun checklistKey(id: Int) = intPreferencesKey("ws_${id}_checklist_mask")
    private fun waterKey(id: Int) = intPreferencesKey("ws_${id}_water_count")
    private fun habitKey(id: Int) = intPreferencesKey("ws_${id}_habit_streak")
    private fun quoteKey(id: Int) = intPreferencesKey("ws_${id}_quote_idx")
    private fun diceKey(id: Int) = intPreferencesKey("ws_${id}_dice_val")
    private fun coinKey(id: Int) = booleanPreferencesKey("ws_${id}_coin_heads")
    private fun tapKey(id: Int) = intPreferencesKey("ws_${id}_tap_counter")
    private fun tttKey(id: Int) = stringPreferencesKey("ws_${id}_ttt_board")
    private fun calcDisplayKey(id: Int) = stringPreferencesKey("ws_${id}_calc_display")
    private fun calcOpKey(id: Int) = stringPreferencesKey("ws_${id}_calc_op")
    private fun calcAccKey(id: Int) = doublePreferencesKey("ws_${id}_calc_acc")
    private fun pomoRunKey(id: Int) = booleanPreferencesKey("ws_${id}_pomo_run")
    private fun pomoMinKey(id: Int) = intPreferencesKey("ws_${id}_pomo_min")
    private fun musicPlayKey(id: Int) = booleanPreferencesKey("ws_${id}_music_play")
    private fun musicTrackKey(id: Int) = intPreferencesKey("ws_${id}_music_track")

    fun observeAmbientSoundEnabled(context: Context): Flow<Boolean> =
        context.glassWidgetDataStore.data.map { prefs -> prefs[KEY_AMBIENT_SOUND] ?: true }

    suspend fun setAmbientSoundEnabled(context: Context, enabled: Boolean) {
        context.glassWidgetDataStore.edit { prefs ->
            prefs[KEY_AMBIENT_SOUND] = enabled
        }
    }

    suspend fun savePendingPinConfig(context: Context, config: WidgetInstanceConfig) {
        context.glassWidgetDataStore.edit { prefs ->
            prefs[KEY_PENDING_PIN_PRESET] = config.presetId
            prefs[KEY_PENDING_PIN_THEME] = config.glassTheme.name
            prefs[KEY_PENDING_PIN_OPACITY] = config.opacity
            prefs[KEY_PENDING_PIN_RADIUS] = config.cornerRadiusDp
            prefs[KEY_PENDING_PIN_ACCENT] = config.accentColorArgb
            prefs[KEY_PENDING_PIN_TEXT_COLOR] = config.textColorArgb
            prefs[KEY_PENDING_PIN_CUSTOM_TEXT] = config.customText
            prefs[KEY_PENDING_PIN_TARGET_DATE] = config.targetDateEpochMillis
            prefs[KEY_PENDING_PIN_APPS] = config.selectedApps.joinToString("|")
            prefs[KEY_PENDING_PIN_PHOTO_URI] = config.photoUri
        }
    }

    suspend fun saveWidgetConfig(context: Context, config: WidgetInstanceConfig) {
        val id = config.appWidgetId
        if (id < 0) return
        context.glassWidgetDataStore.edit { prefs ->
            prefs[presetKey(id)] = config.presetId
            prefs[categoryKey(id)] = config.category.name
            prefs[themeKey(id)] = config.glassTheme.name
            prefs[opacityKey(id)] = config.opacity.coerceIn(0.10f, 0.90f)
            prefs[radiusKey(id)] = config.cornerRadiusDp.coerceIn(12, 32)
            prefs[accentKey(id)] = config.accentColorArgb
            prefs[textColorKey(id)] = config.textColorArgb
            prefs[customTextKey(id)] = config.customText
            prefs[targetDateKey(id)] = config.targetDateEpochMillis
            prefs[selectedAppsKey(id)] = config.selectedApps.joinToString("|")
            prefs[photoUriKey(id)] = config.photoUri
        }
    }

    suspend fun getWidgetConfig(
        context: Context,
        appWidgetId: Int,
        fallbackSlot: WidgetSlotSize = WidgetSlotSize.SIZE_2X2
    ): WidgetInstanceConfig {
        val prefs = context.glassWidgetDataStore.data.first()
        val savedPresetId = prefs[presetKey(appWidgetId)]
        if (savedPresetId != null) {
            val spec = WidgetPresetRegistry.getById(savedPresetId)
            val category = prefs[categoryKey(appWidgetId)]
                ?.let { runCatching { WidgetCategory.valueOf(it) }.getOrNull() }
                ?: spec.category
            val theme = prefs[themeKey(appWidgetId)]
                ?.let { runCatching { GlassTheme.valueOf(it) }.getOrNull() }
                ?: spec.defaultTheme
            val appsRaw = prefs[selectedAppsKey(appWidgetId)].orEmpty()
            return WidgetInstanceConfig(
                appWidgetId = appWidgetId,
                presetId = spec.id,
                category = category,
                glassTheme = theme,
                opacity = (prefs[opacityKey(appWidgetId)] ?: 0.44f).coerceIn(0.10f, 0.90f),
                cornerRadiusDp = (prefs[radiusKey(appWidgetId)] ?: 24).coerceIn(12, 32),
                accentColorArgb = prefs[accentKey(appWidgetId)] ?: spec.defaultAccentArgb,
                textColorArgb = prefs[textColorKey(appWidgetId)]
                    ?: if (theme == GlassTheme.LIGHT_FROSTED) 0xFF0F172AL else 0xFFFFFFFFL,
                customText = prefs[customTextKey(appWidgetId)].orEmpty(),
                targetDateEpochMillis = prefs[targetDateKey(appWidgetId)]
                    ?: (System.currentTimeMillis() + 14L * 86_400_000L),
                selectedApps = if (appsRaw.isBlank()) emptyList() else appsRaw.split("|").filter { it.isNotBlank() },
                photoUri = prefs[photoUriKey(appWidgetId)].orEmpty()
            )
        }

        // If pinned from gallery without explicit config screen, adopt pending pin config
        val pendingId = prefs[KEY_PENDING_PIN_PRESET]
        if (pendingId != null) {
            val spec = WidgetPresetRegistry.getById(pendingId)
            val theme = prefs[KEY_PENDING_PIN_THEME]
                ?.let { runCatching { GlassTheme.valueOf(it) }.getOrNull() }
                ?: spec.defaultTheme
            val appsRaw = prefs[KEY_PENDING_PIN_APPS].orEmpty()
            val adopted = WidgetInstanceConfig(
                appWidgetId = appWidgetId,
                presetId = spec.id,
                category = spec.category,
                glassTheme = theme,
                opacity = (prefs[KEY_PENDING_PIN_OPACITY] ?: 0.44f).coerceIn(0.10f, 0.90f),
                cornerRadiusDp = (prefs[KEY_PENDING_PIN_RADIUS] ?: 24).coerceIn(12, 32),
                accentColorArgb = prefs[KEY_PENDING_PIN_ACCENT] ?: spec.defaultAccentArgb,
                textColorArgb = prefs[KEY_PENDING_PIN_TEXT_COLOR]
                    ?: if (theme == GlassTheme.LIGHT_FROSTED) 0xFF0F172AL else 0xFFFFFFFFL,
                customText = prefs[KEY_PENDING_PIN_CUSTOM_TEXT].orEmpty(),
                targetDateEpochMillis = prefs[KEY_PENDING_PIN_TARGET_DATE]
                    ?: (System.currentTimeMillis() + 14L * 86_400_000L),
                selectedApps = if (appsRaw.isBlank()) emptyList() else appsRaw.split("|").filter { it.isNotBlank() },
                photoUri = prefs[KEY_PENDING_PIN_PHOTO_URI].orEmpty()
            )
            if (appWidgetId >= 0) {
                saveWidgetConfig(context, adopted)
            }
            return adopted
        }

        val defaultSpec = WidgetPresetRegistry.defaultPresetForSlot(fallbackSlot)
        return WidgetInstanceConfig(
            appWidgetId = appWidgetId,
            presetId = defaultSpec.id,
            category = defaultSpec.category,
            glassTheme = defaultSpec.defaultTheme,
            opacity = 0.44f,
            cornerRadiusDp = 24,
            accentColorArgb = defaultSpec.defaultAccentArgb,
            textColorArgb = if (defaultSpec.defaultTheme == GlassTheme.LIGHT_FROSTED) 0xFF0F172AL else 0xFFFFFFFFL
        )
    }

    suspend fun getInteractiveState(context: Context, appWidgetId: Int): WidgetInteractiveState {
        val prefs = context.glassWidgetDataStore.data.first()
        return WidgetInteractiveState(
            checklistMask = prefs[checklistKey(appWidgetId)] ?: 0b00101,
            waterCount = prefs[waterKey(appWidgetId)] ?: 5,
            habitStreak = prefs[habitKey(appWidgetId)] ?: 12,
            quoteIndex = prefs[quoteKey(appWidgetId)] ?: 0,
            diceValue = prefs[diceKey(appWidgetId)] ?: 6,
            coinHeads = prefs[coinKey(appWidgetId)] ?: true,
            tapCounter = prefs[tapKey(appWidgetId)] ?: 24,
            tttBoard = prefs[tttKey(appWidgetId)] ?: "X_O_X____",
            calcDisplay = prefs[calcDisplayKey(appWidgetId)] ?: "0",
            calcPendingOp = prefs[calcOpKey(appWidgetId)].orEmpty(),
            calcAccumulator = prefs[calcAccKey(appWidgetId)] ?: 0.0,
            pomodoroRunning = prefs[pomoRunKey(appWidgetId)] ?: false,
            pomodoroMinutes = prefs[pomoMinKey(appWidgetId)] ?: 25,
            musicPlaying = prefs[musicPlayKey(appWidgetId)] ?: true,
            musicTrackIndex = prefs[musicTrackKey(appWidgetId)] ?: 0
        )
    }

    suspend fun updateInteractiveState(
        context: Context,
        appWidgetId: Int,
        transform: (WidgetInteractiveState) -> WidgetInteractiveState
    ): WidgetInteractiveState {
        val current = getInteractiveState(context, appWidgetId)
        val updated = transform(current)
        context.glassWidgetDataStore.edit { prefs ->
            prefs[checklistKey(appWidgetId)] = updated.checklistMask
            prefs[waterKey(appWidgetId)] = updated.waterCount
            prefs[habitKey(appWidgetId)] = updated.habitStreak
            prefs[quoteKey(appWidgetId)] = updated.quoteIndex
            prefs[diceKey(appWidgetId)] = updated.diceValue
            prefs[coinKey(appWidgetId)] = updated.coinHeads
            prefs[tapKey(appWidgetId)] = updated.tapCounter
            prefs[tttKey(appWidgetId)] = updated.tttBoard
            prefs[calcDisplayKey(appWidgetId)] = updated.calcDisplay
            prefs[calcOpKey(appWidgetId)] = updated.calcPendingOp
            prefs[calcAccKey(appWidgetId)] = updated.calcAccumulator
            prefs[pomoRunKey(appWidgetId)] = updated.pomodoroRunning
            prefs[pomoMinKey(appWidgetId)] = updated.pomodoroMinutes
            prefs[musicPlayKey(appWidgetId)] = updated.musicPlaying
            prefs[musicTrackKey(appWidgetId)] = updated.musicTrackIndex
        }
        return updated
    }

    suspend fun deleteWidgetConfig(context: Context, appWidgetId: Int) {
        context.glassWidgetDataStore.edit { prefs ->
            prefs.remove(presetKey(appWidgetId))
            prefs.remove(categoryKey(appWidgetId))
            prefs.remove(themeKey(appWidgetId))
            prefs.remove(opacityKey(appWidgetId))
            prefs.remove(radiusKey(appWidgetId))
            prefs.remove(accentKey(appWidgetId))
            prefs.remove(textColorKey(appWidgetId))
            prefs.remove(customTextKey(appWidgetId))
            prefs.remove(targetDateKey(appWidgetId))
            prefs.remove(selectedAppsKey(appWidgetId))
            prefs.remove(photoUriKey(appWidgetId))
            prefs.remove(checklistKey(appWidgetId))
            prefs.remove(waterKey(appWidgetId))
            prefs.remove(habitKey(appWidgetId))
            prefs.remove(quoteKey(appWidgetId))
            prefs.remove(diceKey(appWidgetId))
            prefs.remove(coinKey(appWidgetId))
            prefs.remove(tapKey(appWidgetId))
            prefs.remove(tttKey(appWidgetId))
            prefs.remove(calcDisplayKey(appWidgetId))
            prefs.remove(calcOpKey(appWidgetId))
            prefs.remove(calcAccKey(appWidgetId))
            prefs.remove(pomoRunKey(appWidgetId))
            prefs.remove(pomoMinKey(appWidgetId))
            prefs.remove(musicPlayKey(appWidgetId))
            prefs.remove(musicTrackKey(appWidgetId))
        }
    }
}
