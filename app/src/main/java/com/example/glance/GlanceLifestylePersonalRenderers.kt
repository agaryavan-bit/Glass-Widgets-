package com.example.glance

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.data.DeviceTelemetryProvider
import com.example.model.WidgetInstanceConfig
import com.example.model.WidgetInteractiveState
import com.example.model.WidgetPresetSpec
import com.example.model.WidgetSlotSize
import com.example.render.GlanceBitmapPainter
import com.example.render.GlassResolvedColors

@Composable
fun GlanceFinance(spec: WidgetPresetSpec, tokens: GlassResolvedColors) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val secondary = ColorProvider(Color(tokens.textSecondaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))
    val pair = when (spec.layoutVariant % 3) {
        0 -> "BTC  $94,280  +2.4%" to "ETH  $3,410  +1.8%"
        1 -> "GOLD  $2,684  +0.9%" to "EUR/USD  1.084"
        else -> "S&P 500  5,982  +0.6%" to "NASDAQ  19,140"
    }
    Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Text("MARKETS (CACHED)", style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
        Spacer(GlanceModifier.height(4.dp))
        Text(pair.first, style = TextStyle(color = primary, fontSize = 13.sp, fontWeight = FontWeight.Bold))
        Text(pair.second, style = TextStyle(color = secondary, fontSize = 12.sp))
    }
}

@Composable
fun GlanceBluetooth(config: WidgetInstanceConfig, tokens: GlassResolvedColors) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val secondary = ColorProvider(Color(tokens.textSecondaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .clickable(actionRunCallback<LaunchShortcutAction>(actionParametersOf(PARAM_ACTION_TYPE to "settings_bt"))),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("🎧 ${config.customText.ifBlank { "AirBuds Pro" }}", style = TextStyle(color = accent, fontSize = 11.sp, fontWeight = FontWeight.Bold))
        Spacer(GlanceModifier.height(4.dp))
        Text("L 92%  ·  R 88%  ·  Case 76%", style = TextStyle(color = primary, fontSize = 13.sp, fontWeight = FontWeight.Bold))
        Text("Tap to open Bluetooth settings", style = TextStyle(color = secondary, fontSize = 10.sp))
    }
}

@Composable
fun GlanceSocial(tokens: GlassResolvedColors) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))
    val links = listOf(
        "WA" to "https://web.whatsapp.com",
        "IG" to "https://instagram.com",
        "X" to "https://x.com",
        "TG" to "https://telegram.org"
    )
    Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Text("SOCIAL HUB", style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
        Spacer(GlanceModifier.height(6.dp))
        Row(modifier = GlanceModifier.fillMaxWidth()) {
            links.forEach { (code, url) ->
                Column(
                    modifier = GlanceModifier
                        .defaultWeight()
                        .padding(3.dp)
                        .background(Color(tokens.chipFillArgb))
                        .clickable(actionRunCallback<LaunchShortcutAction>(actionParametersOf(PARAM_ACTION_TYPE to "url", PARAM_PAYLOAD to url)))
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(code, style = TextStyle(color = primary, fontSize = 12.sp, fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Composable
fun GlanceGames(
    appWidgetId: Int,
    slotSize: WidgetSlotSize,
    spec: WidgetPresetSpec,
    interactive: WidgetInteractiveState,
    tokens: GlassResolvedColors
) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))

    when (spec.layoutVariant % 3) {
        0 -> Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .clickable(
                    actionRunCallback<InteractiveWidgetAction>(
                        actionParametersOf(PARAM_WIDGET_ID to appWidgetId, PARAM_SLOT_ID to slotSize.id, PARAM_ACTION_TYPE to "dice_roll")
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🎲 TAP TO ROLL", style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
            Text("${interactive.diceValue}", style = TextStyle(color = primary, fontSize = 36.sp, fontWeight = FontWeight.Bold))
        }
        1 -> Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .clickable(
                    actionRunCallback<InteractiveWidgetAction>(
                        actionParametersOf(PARAM_WIDGET_ID to appWidgetId, PARAM_SLOT_ID to slotSize.id, PARAM_ACTION_TYPE to "coin_flip")
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🪙 COIN FLIP", style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
            Text(if (interactive.coinHeads) "HEADS" else "TAILS", style = TextStyle(color = primary, fontSize = 24.sp, fontWeight = FontWeight.Bold))
        }
        else -> Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .clickable(
                    actionRunCallback<InteractiveWidgetAction>(
                        actionParametersOf(PARAM_WIDGET_ID to appWidgetId, PARAM_SLOT_ID to slotSize.id, PARAM_ACTION_TYPE to "tap_inc")
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("TAP COUNTER", style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
            Text("${interactive.tapCounter}", style = TextStyle(color = primary, fontSize = 34.sp, fontWeight = FontWeight.Bold))
        }
    }
}

@Composable
fun GlanceCamera(tokens: GlassResolvedColors) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))
    Row(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        listOf("Photo" to "camera_photo", "Video" to "camera_video", "Selfie" to "camera_photo", "QR" to "camera_photo").forEach { (label, action) ->
            Column(
                modifier = GlanceModifier
                    .defaultWeight()
                    .padding(3.dp)
                    .background(Color(tokens.chipFillArgb))
                    .clickable(actionRunCallback<LaunchShortcutAction>(actionParametersOf(PARAM_ACTION_TYPE to action)))
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(label, style = TextStyle(color = if (label == "Photo") accent else primary, fontSize = 11.sp, fontWeight = FontWeight.Bold))
            }
        }
    }
}

@Composable
fun GlanceQuotes(
    appWidgetId: Int,
    slotSize: WidgetSlotSize,
    config: WidgetInstanceConfig,
    interactive: WidgetInteractiveState,
    tokens: GlassResolvedColors
) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))
    val quote = DeviceTelemetryProvider.curatedQuotes[interactive.quoteIndex % DeviceTelemetryProvider.curatedQuotes.size]

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .clickable(
                actionRunCallback<InteractiveWidgetAction>(
                    actionParametersOf(PARAM_WIDGET_ID to appWidgetId, PARAM_SLOT_ID to slotSize.id, PARAM_ACTION_TYPE to "quote_next")
                )
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("“${config.customText.ifBlank { quote.text }}”", style = TextStyle(color = primary, fontSize = 13.sp, fontWeight = FontWeight.Medium))
        Spacer(GlanceModifier.height(4.dp))
        Text("— ${quote.author} · Tap for next", style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
    }
}

@Composable
fun GlanceReligious(spec: WidgetPresetSpec, tokens: GlassResolvedColors) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val secondary = ColorProvider(Color(tokens.textSecondaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))

    Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Text("NEXT PRAYER · ASR", style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
        Text("15:46", style = TextStyle(color = primary, fontSize = 26.sp, fontWeight = FontWeight.Bold))
        Text(
            if (spec.layoutVariant % 2 == 0) "Fajr 05:18 · Dhuhr 12:24 · Maghrib 18:12" else DeviceTelemetryProvider.reflectionVerses.first(),
            style = TextStyle(color = secondary, fontSize = 10.sp)
        )
    }
}

@Composable
fun GlanceCompass(spec: WidgetPresetSpec, tokens: GlassResolvedColors) {
    val heading = (32 + spec.layoutVariant * 37) % 360
    val dial = GlanceBitmapPainter.drawCompassDial(160, heading, tokens)
    Row(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Image(provider = ImageProvider(dial), contentDescription = "Compass dial", modifier = GlanceModifier.size(66.dp))
        Spacer(GlanceModifier.width(10.dp))
        Column {
            Text("COMPASS", style = TextStyle(color = ColorProvider(Color(tokens.accentArgb)), fontSize = 10.sp, fontWeight = FontWeight.Bold))
            Text("${heading}° NE", style = TextStyle(color = ColorProvider(Color(tokens.textPrimaryArgb)), fontSize = 22.sp, fontWeight = FontWeight.Bold))
        }
    }
}

@Composable
fun GlanceCalculator(
    appWidgetId: Int,
    slotSize: WidgetSlotSize,
    interactive: WidgetInteractiveState,
    tokens: GlassResolvedColors
) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))

    Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Text(interactive.calcDisplay, style = TextStyle(color = primary, fontSize = 20.sp, fontWeight = FontWeight.Bold))
        Spacer(GlanceModifier.height(6.dp))
        Row(modifier = GlanceModifier.fillMaxWidth()) {
            listOf("7", "8", "9", "+", "C", "=").forEach { key ->
                Column(
                    modifier = GlanceModifier
                        .defaultWeight()
                        .padding(2.dp)
                        .background(Color(tokens.chipFillArgb))
                        .clickable(
                            actionRunCallback<InteractiveWidgetAction>(
                                actionParametersOf(
                                    PARAM_WIDGET_ID to appWidgetId,
                                    PARAM_SLOT_ID to slotSize.id,
                                    PARAM_ACTION_TYPE to "calc_key",
                                    PARAM_PAYLOAD to key
                                )
                            )
                        )
                        .padding(vertical = 7.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(key, style = TextStyle(color = if (key == "=" || key == "+") accent else primary, fontSize = 12.sp, fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Composable
fun GlancePersonalise(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    tokens: GlassResolvedColors
) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))
    val text = config.customText.ifBlank { "Liquid Glass" }

    when (spec.layoutVariant) {
        1, 4 -> {
            val qr = GlanceBitmapPainter.drawDeterministicQrCode(160, text, tokens)
            Row(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Image(provider = ImageProvider(qr), contentDescription = "Custom QR Code", modifier = GlanceModifier.size(72.dp))
                Spacer(GlanceModifier.width(10.dp))
                Column {
                    Text("QR CODE", style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
                    Text(text, style = TextStyle(color = primary, fontSize = 13.sp, fontWeight = FontWeight.Bold))
                }
            }
        }
        2, 9 -> {
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("✦", style = TextStyle(color = accent, fontSize = 22.sp))
            }
        }
        else -> {
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text, style = TextStyle(color = primary, fontSize = 22.sp, fontWeight = FontWeight.Bold))
            }
        }
    }
}
