package com.example.glance

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import com.example.data.DeviceTelemetryProvider
import com.example.data.WidgetConfigStore
import com.example.data.WidgetTelemetrySnapshot
import com.example.model.WidgetCategory
import com.example.model.WidgetInstanceConfig
import com.example.model.WidgetInteractiveState
import com.example.model.WidgetPresetRegistry
import com.example.model.WidgetPresetSpec
import com.example.model.WidgetSlotSize
import com.example.render.GlanceBitmapPainter
import com.example.render.GlassResolvedColors
import com.example.render.GlassVisualTokens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Shared offline-safe GlanceAppWidget base used by ONLY the 4 registered size slots
 * (2x2, 4x1, 4x2, 4x4) powering all 840 data-driven presets.
 */
abstract class BaseGlassGlanceWidget(
    val slotSize: WidgetSlotSize
) : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val manager = GlanceAppWidgetManager(context)
        val appWidgetId = runCatching { manager.getAppWidgetId(id) }.getOrDefault(-1)

        // 100% offline-safe: reads DataStore & local device telemetry with zero network calls.
        val config = WidgetConfigStore.getWidgetConfig(context, appWidgetId, slotSize)
        val interactive = WidgetConfigStore.getInteractiveState(context, appWidgetId)
        val spec = WidgetPresetRegistry.getById(config.presetId)
        val telemetry = DeviceTelemetryProvider.captureSnapshot(context)
        val tokens = GlassVisualTokens.resolve(context, spec, config)
        val bgBitmap = GlanceBitmapPainter.drawLiquidGlassBackground(slotSize, tokens, spec.borderStyle)

        provideContent {
            GlassWidgetRoot(
                context = context,
                appWidgetId = appWidgetId,
                slotSize = slotSize,
                spec = spec,
                config = config,
                telemetry = telemetry,
                interactive = interactive,
                tokens = tokens,
                bgBitmap = bgBitmap
            )
        }
    }
}

@Composable
private fun GlassWidgetRoot(
    context: Context,
    appWidgetId: Int,
    slotSize: WidgetSlotSize,
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    telemetry: WidgetTelemetrySnapshot,
    interactive: WidgetInteractiveState,
    tokens: GlassResolvedColors,
    bgBitmap: android.graphics.Bitmap
) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .clickable(
                actionRunCallback<LaunchShortcutAction>(
                    actionParametersOf(
                        PARAM_ACTION_TYPE to "config",
                        PARAM_WIDGET_ID to appWidgetId
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            provider = ImageProvider(bgBitmap),
            contentDescription = "Liquid glass background",
            contentScale = ContentScale.FillBounds,
            modifier = GlanceModifier.fillMaxSize()
        )
        Box(
            modifier = GlanceModifier.fillMaxSize().padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            when (spec.category) {
                WidgetCategory.CLOCK_DIGITAL -> GlanceClockDigital(spec, config, telemetry, tokens)
                WidgetCategory.CLOCK_ANALOG -> GlanceClockAnalog(spec, telemetry, tokens)
                WidgetCategory.CLOCK_HYBRID -> GlanceClockHybrid(spec, telemetry, tokens)
                WidgetCategory.CALENDAR -> GlanceCalendar(spec, config, telemetry, tokens)
                WidgetCategory.QUICK_SETTINGS -> GlanceQuickSettings(tokens)
                WidgetCategory.GOOGLE -> GlanceGoogle(config, tokens)
                WidgetCategory.PHOTO -> GlancePhoto(context, config, tokens)
                WidgetCategory.WEATHER -> GlanceWeather(spec, config, tokens)
                WidgetCategory.DEVICE -> GlanceDevice(telemetry, tokens)
                WidgetCategory.BATTERY -> GlanceBattery(telemetry, tokens)
                WidgetCategory.CONTACTS -> GlanceContacts(config, tokens)
                WidgetCategory.AI -> GlanceAi(tokens)
                WidgetCategory.PRODUCTIVITY -> GlanceProductivity(appWidgetId, slotSize, spec, config, telemetry, interactive, tokens)
                WidgetCategory.NOTES_TODO -> GlanceNotesTodo(appWidgetId, slotSize, spec, config, interactive, tokens)
                WidgetCategory.APP_LAUNCHER -> GlanceAppLauncher(config, tokens)
                WidgetCategory.FOLDER_APPS -> GlanceFolderApps(spec, config, tokens)
                WidgetCategory.MUSIC -> GlanceMusic(context, appWidgetId, slotSize, interactive, tokens)
                WidgetCategory.HEALTH -> GlanceHealth(appWidgetId, slotSize, spec, interactive, tokens)
                WidgetCategory.FINANCE -> GlanceFinance(spec, tokens)
                WidgetCategory.BLUETOOTH -> GlanceBluetooth(config, tokens)
                WidgetCategory.SOCIAL -> GlanceSocial(tokens)
                WidgetCategory.GAMES -> GlanceGames(appWidgetId, slotSize, spec, interactive, tokens)
                WidgetCategory.CAMERA -> GlanceCamera(tokens)
                WidgetCategory.QUOTES -> GlanceQuotes(appWidgetId, slotSize, config, interactive, tokens)
                WidgetCategory.RELIGIOUS -> GlanceReligious(spec, tokens)
                WidgetCategory.COMPASS -> GlanceCompass(spec, tokens)
                WidgetCategory.CALCULATOR -> GlanceCalculator(appWidgetId, slotSize, interactive, tokens)
                WidgetCategory.PERSONALISE -> GlancePersonalise(spec, config, tokens)
            }
        }
    }
}

class GlassWidget2x2 : BaseGlassGlanceWidget(WidgetSlotSize.SIZE_2X2)
class GlassWidget4x1 : BaseGlassGlanceWidget(WidgetSlotSize.SIZE_4X1)
class GlassWidget4x2 : BaseGlassGlanceWidget(WidgetSlotSize.SIZE_4X2)
class GlassWidget4x4 : BaseGlassGlanceWidget(WidgetSlotSize.SIZE_4X4)

abstract class BaseGlassWidgetReceiver(
    private val widgetInstance: BaseGlassGlanceWidget
) : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget get() = widgetInstance

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        CoroutineScope(Dispatchers.IO).launch {
            appWidgetIds.forEach { id ->
                WidgetConfigStore.deleteWidgetConfig(context, id)
            }
        }
    }
}

class GlassWidget2x2Receiver : BaseGlassWidgetReceiver(GlassWidget2x2())
class GlassWidget4x1Receiver : BaseGlassWidgetReceiver(GlassWidget4x1())
class GlassWidget4x2Receiver : BaseGlassWidgetReceiver(GlassWidget4x2())
class GlassWidget4x4Receiver : BaseGlassWidgetReceiver(GlassWidget4x4())

object GlassWidgetSlots {
    fun widgetForSlotId(slotId: String): BaseGlassGlanceWidget = when (slotId) {
        WidgetSlotSize.SIZE_4X1.id -> GlassWidget4x1()
        WidgetSlotSize.SIZE_4X2.id -> GlassWidget4x2()
        WidgetSlotSize.SIZE_4X4.id -> GlassWidget4x4()
        else -> GlassWidget2x2()
    }

    fun receiverClassForSlot(slotSize: WidgetSlotSize): Class<out GlanceAppWidgetReceiver> = when (slotSize) {
        WidgetSlotSize.SIZE_2X2 -> GlassWidget2x2Receiver::class.java
        WidgetSlotSize.SIZE_4X1 -> GlassWidget4x1Receiver::class.java
        WidgetSlotSize.SIZE_4X2 -> GlassWidget4x2Receiver::class.java
        WidgetSlotSize.SIZE_4X4 -> GlassWidget4x4Receiver::class.java
    }

    /**
     * Updates the specific placed widget by appWidgetId via GlanceAppWidgetManager.getGlanceIdBy(appWidgetId).
     */
    suspend fun updateAppWidgetById(context: Context, appWidgetId: Int) {
        val manager = GlanceAppWidgetManager(context)
        val glanceId = runCatching { manager.getGlanceIdBy(appWidgetId) }.getOrNull() ?: return
        // Trigger update across the matching provider
        listOf(GlassWidget2x2(), GlassWidget4x1(), GlassWidget4x2(), GlassWidget4x4()).forEach { widget ->
            runCatching { widget.update(context, glanceId) }
        }
    }
}
