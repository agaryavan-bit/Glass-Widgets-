package com.example.glance

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
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
import com.example.data.WidgetTelemetrySnapshot
import com.example.model.WidgetInstanceConfig
import com.example.model.WidgetPresetSpec
import com.example.render.GlanceBitmapPainter
import com.example.render.GlassResolvedColors

@Composable
fun GlanceClockDigital(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    telemetry: WidgetTelemetrySnapshot,
    tokens: GlassResolvedColors
) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val secondary = ColorProvider(Color(tokens.textSecondaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))

    when (spec.layoutVariant) {
        2 -> Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(telemetry.hour24, style = TextStyle(color = primary, fontSize = 36.sp, fontWeight = FontWeight.Bold))
            Text(telemetry.minute, style = TextStyle(color = accent, fontSize = 36.sp, fontWeight = FontWeight.Bold))
        }
        3 -> Row(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = GlanceModifier.background(Color(tokens.chipFillArgb)).padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(telemetry.hour24, style = TextStyle(color = primary, fontSize = 32.sp, fontWeight = FontWeight.Bold))
            }
            Spacer(GlanceModifier.width(8.dp))
            Column(
                modifier = GlanceModifier.background(Color(tokens.chipFillArgb)).padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(telemetry.minute, style = TextStyle(color = accent, fontSize = 32.sp, fontWeight = FontWeight.Bold))
            }
        }
        5 -> Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Text("LOCAL  ${telemetry.hour24}:${telemetry.minute}", style = TextStyle(color = primary, fontSize = 22.sp, fontWeight = FontWeight.Bold))
            Spacer(GlanceModifier.height(6.dp))
            Row(modifier = GlanceModifier.fillMaxWidth()) {
                Text("TYO ${telemetry.tokyoTime}", style = TextStyle(color = accent, fontSize = 12.sp, fontWeight = FontWeight.Medium))
                Spacer(GlanceModifier.width(12.dp))
                Text("LDN ${telemetry.londonTime}", style = TextStyle(color = secondary, fontSize = 12.sp))
            }
        }
        else -> Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = config.customText.ifBlank { "${telemetry.weekdayShort}, ${telemetry.monthShort} ${telemetry.dayOfMonth}" },
                style = TextStyle(color = secondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            )
            Spacer(GlanceModifier.height(2.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "${telemetry.hour12}:${telemetry.minute}",
                    style = TextStyle(color = primary, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(GlanceModifier.width(4.dp))
                Text(telemetry.amPm, style = TextStyle(color = accent, fontSize = 12.sp, fontWeight = FontWeight.Bold))
            }
        }
    }
}

@Composable
fun GlanceClockAnalog(
    spec: WidgetPresetSpec,
    telemetry: WidgetTelemetrySnapshot,
    tokens: GlassResolvedColors
) {
    val dial = GlanceBitmapPainter.drawAnalogClockDial(
        sizePx = 180,
        hour = telemetry.hourInt,
        minute = telemetry.minuteInt,
        layoutVariant = spec.layoutVariant,
        tokens = tokens
    )
    Column(
        modifier = GlanceModifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(provider = ImageProvider(dial), contentDescription = "Analog glass clock", modifier = GlanceModifier.size(92.dp))
        Spacer(GlanceModifier.height(4.dp))
        Text(
            "${telemetry.weekdayShort} ${telemetry.dayOfMonth}",
            style = TextStyle(color = ColorProvider(Color(tokens.textSecondaryArgb)), fontSize = 10.sp)
        )
    }
}

@Composable
fun GlanceClockHybrid(
    spec: WidgetPresetSpec,
    telemetry: WidgetTelemetrySnapshot,
    tokens: GlassResolvedColors
) {
    val dial = GlanceBitmapPainter.drawAnalogClockDial(140, telemetry.hourInt, telemetry.minuteInt, spec.layoutVariant, tokens)
    Row(
        modifier = GlanceModifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(provider = ImageProvider(dial), contentDescription = "Hybrid dial", modifier = GlanceModifier.size(64.dp))
        Spacer(GlanceModifier.width(10.dp))
        Column(modifier = GlanceModifier.defaultWeight()) {
            Text("${telemetry.hour24}:${telemetry.minute}", style = TextStyle(color = ColorProvider(Color(tokens.textPrimaryArgb)), fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text("${telemetry.weekdayShort} · ${telemetry.batteryPercent}% ⚡", style = TextStyle(color = ColorProvider(Color(tokens.accentArgb)), fontSize = 11.sp, fontWeight = FontWeight.Medium))
        }
    }
}

@Composable
fun GlanceCalendar(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    telemetry: WidgetTelemetrySnapshot,
    tokens: GlassResolvedColors
) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val secondary = ColorProvider(Color(tokens.textSecondaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))

    when (spec.layoutVariant) {
        4 -> {
            val ring = GlanceBitmapPainter.drawProgressRing(150, telemetry.dayProgress, "${(telemetry.dayProgress * 100).toInt()}%", tokens)
            Row(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Image(provider = ImageProvider(ring), contentDescription = "Day progress", modifier = GlanceModifier.size(68.dp))
                Spacer(GlanceModifier.width(10.dp))
                Column {
                    Text("DAY PROGRESS", style = TextStyle(color = secondary, fontSize = 10.sp))
                    Text("${telemetry.weekdayShort} ${telemetry.dayOfMonth}", style = TextStyle(color = primary, fontSize = 16.sp, fontWeight = FontWeight.Bold))
                }
            }
        }
        5 -> {
            val pct = (telemetry.yearProgress * 100).toInt()
            Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Text("${telemetry.year} YEAR PROGRESS", style = TextStyle(color = secondary, fontSize = 10.sp))
                Spacer(GlanceModifier.height(4.dp))
                Text("$pct% Complete", style = TextStyle(color = accent, fontSize = 22.sp, fontWeight = FontWeight.Bold))
            }
        }
        8 -> {
            val daysLeft = DeviceTelemetryProvider.getDaysUntil(config.targetDateEpochMillis)
            Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Text(config.customText.ifBlank { "MILESTONE COUNTDOWN" }, style = TextStyle(color = secondary, fontSize = 10.sp))
                Text("$daysLeft Days", style = TextStyle(color = accent, fontSize = 28.sp, fontWeight = FontWeight.Bold))
            }
        }
        else -> {
            Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Text(telemetry.weekdayFull.uppercase(), style = TextStyle(color = accent, fontSize = 11.sp, fontWeight = FontWeight.Bold))
                Text("${telemetry.dayOfMonth}", style = TextStyle(color = primary, fontSize = 36.sp, fontWeight = FontWeight.Bold))
                Text("${telemetry.monthFull} ${telemetry.year}", style = TextStyle(color = secondary, fontSize = 11.sp))
            }
        }
    }
}
