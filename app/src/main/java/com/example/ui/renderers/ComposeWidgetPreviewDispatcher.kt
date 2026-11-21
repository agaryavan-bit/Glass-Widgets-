package com.example.ui.renderers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.data.WidgetTelemetrySnapshot
import com.example.model.WidgetCategory
import com.example.model.WidgetInstanceConfig
import com.example.model.WidgetInteractiveState
import com.example.model.WidgetPresetSpec

/**
 * Central dispatcher routing any of the 840 data-driven WidgetPresetSpecs to its
 * corresponding modular Category Renderer inside a LiquidGlassSurface.
 */
@Composable
fun LiveGlassWidgetPreview(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    telemetry: WidgetTelemetrySnapshot,
    interactiveState: WidgetInteractiveState = WidgetInteractiveState(),
    modifier: Modifier = Modifier
) {
    LiquidGlassSurface(
        spec = spec,
        config = config,
        modifier = modifier
    ) { tokens ->
        when (spec.category) {
            WidgetCategory.CLOCK_DIGITAL -> RenderClockDigitalPreview(spec, config, telemetry, tokens)
            WidgetCategory.CLOCK_ANALOG -> RenderClockAnalogPreview(spec, telemetry, tokens)
            WidgetCategory.CLOCK_HYBRID -> RenderClockHybridPreview(spec, telemetry, tokens)
            WidgetCategory.CALENDAR -> RenderCalendarPreview(spec, config, telemetry, tokens)
            WidgetCategory.QUICK_SETTINGS -> RenderQuickSettingsPreview(spec, tokens)
            WidgetCategory.GOOGLE -> RenderGooglePreview(spec, config, tokens)
            WidgetCategory.PHOTO -> RenderPhotoPreview(spec, config, tokens)
            WidgetCategory.WEATHER -> RenderWeatherPreview(spec, config, tokens)
            WidgetCategory.DEVICE -> RenderDevicePreview(spec, telemetry, tokens)
            WidgetCategory.BATTERY -> RenderBatteryPreview(spec, telemetry, tokens)
            WidgetCategory.CONTACTS -> RenderContactsPreview(spec, config, tokens)
            WidgetCategory.AI -> RenderAiPreview(spec, config, tokens)
            WidgetCategory.PRODUCTIVITY -> RenderProductivityPreview(spec, config, telemetry, interactiveState, tokens)
            WidgetCategory.NOTES_TODO -> RenderNotesTodoPreview(spec, config, interactiveState, tokens)
            WidgetCategory.APP_LAUNCHER -> RenderAppLauncherPreview(spec, config, tokens)
            WidgetCategory.FOLDER_APPS -> RenderFolderAppsPreview(spec, tokens)
            WidgetCategory.MUSIC -> RenderMusicPreview(spec, interactiveState, tokens)
            WidgetCategory.HEALTH -> RenderHealthPreview(spec, interactiveState, tokens)
            WidgetCategory.FINANCE -> RenderFinancePreview(spec, tokens)
            WidgetCategory.BLUETOOTH -> RenderBluetoothPreview(spec, config, tokens)
            WidgetCategory.SOCIAL -> RenderSocialPreview(spec, tokens)
            WidgetCategory.GAMES -> RenderGamesPreview(spec, interactiveState, tokens)
            WidgetCategory.CAMERA -> RenderCameraPreview(spec, tokens)
            WidgetCategory.QUOTES -> RenderQuotesPreview(spec, config, interactiveState, tokens)
            WidgetCategory.RELIGIOUS -> RenderReligiousPreview(spec, tokens)
            WidgetCategory.COMPASS -> RenderCompassPreview(spec, tokens)
            WidgetCategory.CALCULATOR -> RenderCalculatorPreview(spec, interactiveState, tokens)
            WidgetCategory.PERSONALISE -> RenderPersonalisePreview(spec, config, tokens)
        }
    }
}
