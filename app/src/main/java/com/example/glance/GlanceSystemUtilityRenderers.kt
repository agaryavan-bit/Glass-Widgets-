package com.example.glance

import android.content.Context
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
import androidx.glance.layout.ContentScale
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
import com.example.R
import com.example.data.WidgetTelemetrySnapshot
import com.example.model.WidgetInstanceConfig
import com.example.model.WidgetPresetSpec
import com.example.render.GlanceBitmapPainter
import com.example.render.GlassResolvedColors

@Composable
fun GlanceQuickSettings(
    tokens: GlassResolvedColors
) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))
    val tiles = listOf(
        "Wi-Fi" to "settings_wifi",
        "BT" to "settings_bt",
        "Sound" to "settings_sound",
        "Display" to "settings_display"
    )
    Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Text("QUICK SETTINGS", style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
        Spacer(GlanceModifier.height(6.dp))
        Row(modifier = GlanceModifier.fillMaxWidth()) {
            tiles.forEach { (label, action) ->
                Column(
                    modifier = GlanceModifier
                        .defaultWeight()
                        .padding(3.dp)
                        .background(Color(tokens.chipFillArgb))
                        .clickable(actionRunCallback<LaunchShortcutAction>(actionParametersOf(PARAM_ACTION_TYPE to action)))
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(label, style = TextStyle(color = primary, fontSize = 11.sp, fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Composable
fun GlanceGoogle(
    config: WidgetInstanceConfig,
    tokens: GlassResolvedColors
) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))
    val shortcuts = listOf(
        "Search" to "https://www.google.com",
        "Gmail" to "https://mail.google.com",
        "Maps" to "https://maps.google.com",
        "Gemini" to "https://gemini.google.com"
    )
    Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            config.customText.ifBlank { "Google Quick Hub" },
            style = TextStyle(color = accent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(GlanceModifier.height(6.dp))
        Row(modifier = GlanceModifier.fillMaxWidth()) {
            shortcuts.forEach { (label, url) ->
                Column(
                    modifier = GlanceModifier
                        .defaultWeight()
                        .padding(3.dp)
                        .background(Color(tokens.chipFillArgb))
                        .clickable(
                            actionRunCallback<LaunchShortcutAction>(
                                actionParametersOf(PARAM_ACTION_TYPE to "url", PARAM_PAYLOAD to url)
                            )
                        )
                        .padding(vertical = 9.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(label, style = TextStyle(color = primary, fontSize = 11.sp, fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Composable
fun GlancePhoto(
    context: Context,
    config: WidgetInstanceConfig,
    tokens: GlassResolvedColors
) {
    val photoBitmap = GlanceBitmapPainter.loadRoundedPhotoBitmap(
        context = context,
        photoUri = config.photoUri,
        fallbackResId = R.drawable.img_sample_photo_1795251487510
    )
    Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Image(
            provider = ImageProvider(photoBitmap),
            contentDescription = "Glass photo frame",
            contentScale = ContentScale.Crop,
            modifier = GlanceModifier.fillMaxWidth().defaultWeight()
        )
        if (config.customText.isNotBlank()) {
            Spacer(GlanceModifier.height(4.dp))
            Text(
                config.customText,
                style = TextStyle(color = ColorProvider(Color(tokens.textPrimaryArgb)), fontSize = 11.sp, fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Composable
fun GlanceWeather(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    tokens: GlassResolvedColors
) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val secondary = ColorProvider(Color(tokens.textSecondaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))
    val city = config.customText.ifBlank { "San Francisco" }

    Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Row(modifier = GlanceModifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = GlanceModifier.defaultWeight()) {
                Text(city, style = TextStyle(color = secondary, fontSize = 11.sp))
                Text("22°C", style = TextStyle(color = primary, fontSize = 30.sp, fontWeight = FontWeight.Bold))
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("☀ Clear", style = TextStyle(color = accent, fontSize = 12.sp, fontWeight = FontWeight.Bold))
                Text("H:24°  L:15°", style = TextStyle(color = secondary, fontSize = 10.sp))
            }
        }
        if (spec.layoutVariant == 3 || spec.layoutVariant == 4) {
            Spacer(GlanceModifier.height(6.dp))
            Text("MON 22° · TUE 24° · WED 19° · THU 21°", style = TextStyle(color = secondary, fontSize = 10.sp))
        }
    }
}

@Composable
fun GlanceDevice(
    telemetry: WidgetTelemetrySnapshot,
    tokens: GlassResolvedColors
) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val secondary = ColorProvider(Color(tokens.textSecondaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))
    val ramRing = GlanceBitmapPainter.drawProgressRing(140, telemetry.ramPercent, "${(telemetry.ramPercent * 100).toInt()}%", tokens)

    Row(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Image(provider = ImageProvider(ramRing), contentDescription = "RAM meter", modifier = GlanceModifier.size(60.dp))
        Spacer(GlanceModifier.width(10.dp))
        Column {
            Text("DEVICE TELEMETRY", style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
            Text(
                String.format("RAM %.1f / %.0f GB", telemetry.ramUsedGb, telemetry.ramTotalGb),
                style = TextStyle(color = primary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
            Text(
                String.format("Disk %.0f / %.0f GB · Up %s", telemetry.storageUsedGb, telemetry.storageTotalGb, telemetry.uptimeFormatted),
                style = TextStyle(color = secondary, fontSize = 10.sp)
            )
        }
    }
}

@Composable
fun GlanceBattery(
    telemetry: WidgetTelemetrySnapshot,
    tokens: GlassResolvedColors
) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val secondary = ColorProvider(Color(tokens.textSecondaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))
    val ring = GlanceBitmapPainter.drawProgressRing(150, telemetry.batteryPercent / 100f, "${telemetry.batteryPercent}%", tokens)

    Row(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Image(provider = ImageProvider(ring), contentDescription = "Battery ring", modifier = GlanceModifier.size(64.dp))
        Spacer(GlanceModifier.width(10.dp))
        Column {
            Text(if (telemetry.isCharging) "CHARGING ⚡" else "BATTERY", style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
            Text("${telemetry.batteryPercent}%", style = TextStyle(color = primary, fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text(String.format("%.1f°C · %d mV", telemetry.batteryTempC, telemetry.batteryVoltageMv), style = TextStyle(color = secondary, fontSize = 10.sp))
        }
    }
}
