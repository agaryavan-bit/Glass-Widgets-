package com.example.ui.renderers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DeviceTelemetryProvider
import com.example.data.WidgetTelemetrySnapshot
import com.example.model.WidgetInstanceConfig
import com.example.model.WidgetPresetSpec
import com.example.render.GlassResolvedColors
import com.example.ui.theme.JetBrainsMonoFontFamily

// 1. CLOCK DIGITAL RENDERER
@Composable
fun RenderClockDigitalPreview(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    telemetry: WidgetTelemetrySnapshot,
    tokens: GlassResolvedColors
) {
    val font = resolveFontFamily(spec.typographyStyle)
    val weight = resolveFontWeight(spec.typographyStyle)
    val hAlign = resolveHorizontalAlignment(spec.alignment)

    when (spec.layoutVariant) {
        2 -> {
            // Stacked vertical hours over minutes
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = hAlign
            ) {
                if (spec.showIcon) {
                    GlassBadgeChip("${telemetry.weekdayShort} ${telemetry.dayOfMonth}", tokens, accented = true)
                    Spacer(Modifier.height(4.dp))
                }
                Text(telemetry.hour24, color = tokens.textPrimary, fontSize = 36.sp, fontWeight = FontWeight.Bold, fontFamily = font, lineHeight = 36.sp)
                Text(telemetry.minute, color = tokens.accent, fontSize = 36.sp, fontWeight = weight, fontFamily = font, lineHeight = 36.sp)
            }
        }
        3 -> {
            // Split-flip glass tiles
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                listOf(telemetry.hour24, telemetry.minute).forEachIndexed { idx, part ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(tokens.chipFill)
                            .border(1.dp, tokens.borderSubtle, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = part,
                            color = if (idx == 0) tokens.textPrimary else tokens.accent,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = font
                        )
                    }
                }
            }
        }
        5 -> {
            // World Dual-Zone
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (spec.showIcon) {
                            Icon(Icons.Rounded.Public, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                        }
                        Text("LOCAL", color = tokens.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Text("${telemetry.hour24}:${telemetry.minute}", color = tokens.textPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = font)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("TOKYO", color = tokens.textSecondary, fontSize = 9.sp)
                        Text(telemetry.tokyoTime, color = tokens.accent, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, fontFamily = JetBrainsMonoFontFamily)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("LONDON", color = tokens.textSecondary, fontSize = 9.sp)
                        Text(telemetry.londonTime, color = tokens.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, fontFamily = JetBrainsMonoFontFamily)
                    }
                }
            }
        }
        4 -> {
            // Seconds pill with live bar
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = hAlign
            ) {
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("${telemetry.hour24}:${telemetry.minute}", color = tokens.textPrimary, fontSize = 28.sp, fontWeight = FontWeight.Bold, fontFamily = font)
                    Text(":${telemetry.second}", color = tokens.accent, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, fontFamily = JetBrainsMonoFontFamily, modifier = Modifier.padding(bottom = 3.dp))
                }
                if (spec.showProgress) {
                    Spacer(Modifier.height(6.dp))
                    GlassProgressBar(progress = telemetry.dayProgress, tokens = tokens, height = 4.dp)
                }
            }
        }
        else -> {
            // Horizon minimal, bold editorial, date strip, desk stand, studio 4x4
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = hAlign
            ) {
                if (spec.showIcon) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Rounded.AccessTime, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(13.dp))
                        Text(
                            config.customText.ifBlank { "${telemetry.weekdayShort}, ${telemetry.monthShort} ${telemetry.dayOfMonth}" },
                            color = tokens.textSecondary,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${telemetry.hour12}:${telemetry.minute}",
                        color = tokens.textPrimary,
                        fontSize = if (spec.layoutVariant == 1 || spec.layoutVariant == 9) 34.sp else 28.sp,
                        fontWeight = weight,
                        fontFamily = font,
                        maxLines = 1
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = telemetry.amPm,
                        color = tokens.accent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                }
                if (spec.showProgress) {
                    GlassProgressBar(progress = telemetry.dayProgress, tokens = tokens, height = 5.dp)
                } else {
                    Text(
                        text = "${telemetry.weekdayFull} · ${telemetry.monthShort} ${telemetry.dayOfMonth}",
                        color = tokens.textSecondary,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

// 2. CLOCK ANALOG RENDERER
@Composable
fun RenderClockAnalogPreview(
    spec: WidgetPresetSpec,
    telemetry: WidgetTelemetrySnapshot,
    tokens: GlassResolvedColors
) {
    when (spec.layoutVariant) {
        4 -> {
            // Dual-Zone Twin Dials
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    GlassAnalogDialCanvas(telemetry.hourInt, telemetry.minuteInt, 1, tokens, size = 62.dp)
                    Spacer(Modifier.height(3.dp))
                    Text("LOCAL", color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    GlassAnalogDialCanvas((telemetry.hourInt + 8) % 24, telemetry.minuteInt, 2, tokens, size = 62.dp)
                    Spacer(Modifier.height(3.dp))
                    Text("GMT+8", color = tokens.accent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        8 -> {
            // Floating Pill Dial
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GlassAnalogDialCanvas(telemetry.hourInt, telemetry.minuteInt, spec.layoutVariant, tokens, size = 50.dp)
                Column(horizontalAlignment = Alignment.End) {
                    Text(telemetry.weekdayFull, color = tokens.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, fontFamily = resolveFontFamily(spec.typographyStyle))
                    Text("${telemetry.monthShort} ${telemetry.dayOfMonth}", color = tokens.accent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        else -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GlassAnalogDialCanvas(
                    hour = telemetry.hourInt,
                    minute = telemetry.minuteInt,
                    layoutVariant = spec.layoutVariant,
                    tokens = tokens,
                    size = if (spec.layoutVariant == 9) 88.dp else 74.dp
                )
                if (spec.showIcon || spec.layoutVariant == 3 || spec.layoutVariant == 7) {
                    Spacer(Modifier.height(4.dp))
                    val caption = if (spec.layoutVariant == 3) "ROMAN · ${telemetry.weekdayShort}" else "${telemetry.weekdayShort} ${telemetry.dayOfMonth}"
                    Text(caption, color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// 3. CLOCK HYBRID RENDERER
@Composable
fun RenderClockHybridPreview(
    spec: WidgetPresetSpec,
    telemetry: WidgetTelemetrySnapshot,
    tokens: GlassResolvedColors
) {
    val font = resolveFontFamily(spec.typographyStyle)
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        GlassAnalogDialCanvas(
            hour = telemetry.hourInt,
            minute = telemetry.minuteInt,
            layoutVariant = spec.layoutVariant % 5,
            tokens = tokens,
            size = if (spec.layoutVariant == 2 || spec.layoutVariant == 6) 46.dp else 60.dp
        )
        Spacer(Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "${telemetry.hour24}:${telemetry.minute}",
                color = tokens.textPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = font,
                maxLines = 1
            )
            when (spec.layoutVariant % 3) {
                1 -> Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    Icon(Icons.Rounded.BatteryChargingFull, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(13.dp))
                    Text("${telemetry.batteryPercent}%", color = tokens.accent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                2 -> Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    Icon(Icons.Rounded.WbSunny, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(13.dp))
                    Text("22° Clear", color = tokens.textSecondary, fontSize = 11.sp)
                }
                else -> Text("${telemetry.weekdayShort} ${telemetry.monthShort} ${telemetry.dayOfMonth}", color = tokens.textSecondary, fontSize = 10.sp)
            }
            if (spec.showProgress) {
                Spacer(Modifier.height(5.dp))
                GlassProgressBar(progress = telemetry.dayProgress, tokens = tokens, height = 4.dp)
            }
        }
    }
}

// 4. CALENDAR RENDERER
@Composable
fun RenderCalendarPreview(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    telemetry: WidgetTelemetrySnapshot,
    tokens: GlassResolvedColors
) {
    val font = resolveFontFamily(spec.typographyStyle)
    when (spec.layoutVariant) {
        1 -> {
            // Week Horizon 7-day strip
            val days = listOf("S", "M", "T", "W", "T", "F", "S")
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Text("${telemetry.monthFull} ${telemetry.year}", color = tokens.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    days.forEachIndexed { idx, d ->
                        val active = idx == (telemetry.dayOfMonth % 7)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (active) tokens.accent.copy(alpha = 0.28f) else tokens.chipFill.copy(alpha = 0.5f))
                                .padding(horizontal = 5.dp, vertical = 4.dp)
                        ) {
                            Text(d, color = if (active) tokens.accent else tokens.textSecondary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            Text("${((telemetry.dayOfMonth - 2 + idx).coerceIn(1, 30))}", color = tokens.textPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        2, 7, 9 -> {
            // Mini Month Grid
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("${telemetry.monthShort} ${telemetry.year}", color = tokens.accent, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = font)
                    if (spec.showIcon) Icon(Icons.Rounded.CalendarMonth, contentDescription = null, tint = tokens.textSecondary, modifier = Modifier.size(13.dp))
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    for (row in 0 until 4) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            for (col in 0 until 7) {
                                val dayNum = row * 7 + col + 1
                                val isToday = dayNum == telemetry.dayOfMonth.coerceAtMost(28)
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(if (isToday) tokens.accent else tokens.chipFill.copy(alpha = 0.18f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$dayNum",
                                        color = if (isToday) tokens.surfaceBottom else tokens.textPrimary,
                                        fontSize = 8.sp,
                                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        4 -> {
            // Day Progress Ring
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                GlassRingGauge(progress = telemetry.dayProgress, label = "${(telemetry.dayProgress * 100).toInt()}%", tokens = tokens, size = 58.dp)
                Column(horizontalAlignment = Alignment.End) {
                    Text("DAY PROGRESS", color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("${telemetry.weekdayShort} ${telemetry.dayOfMonth}", color = tokens.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = font)
                }
            }
        }
        5 -> {
            // Year Progress Bar
            val pct = (telemetry.yearProgress * 100).toInt()
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${telemetry.year} PROGRESS", color = tokens.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("$pct%", color = tokens.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = JetBrainsMonoFontFamily)
                }
                Spacer(Modifier.height(6.dp))
                GlassProgressBar(progress = telemetry.yearProgress, tokens = tokens, height = 8.dp)
            }
        }
        3, 6 -> {
            // Upcoming Agenda
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Icon(Icons.Rounded.Event, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(14.dp))
                    Text("TODAY · ${telemetry.monthShort} ${telemetry.dayOfMonth}", color = tokens.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(tokens.chipFill).padding(horizontal = 8.dp, vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(config.customText.ifBlank { "Design Review" }, color = tokens.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                        Text("10:30", color = tokens.accent, fontSize = 10.sp, fontFamily = JetBrainsMonoFontFamily)
                    }
                    Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(tokens.chipFill.copy(alpha = 0.6f)).padding(horizontal = 8.dp, vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Deep Focus Sprint", color = tokens.textPrimary, fontSize = 11.sp, maxLines = 1)
                        Text("14:00", color = tokens.textSecondary, fontSize = 10.sp, fontFamily = JetBrainsMonoFontFamily)
                    }
                }
            }
        }
        8 -> {
            // Milestone Countdown
            val daysLeft = DeviceTelemetryProvider.getDaysUntil(config.targetDateEpochMillis)
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Text(config.customText.ifBlank { "TARGET MILESTONE" }, color = tokens.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("$daysLeft", color = tokens.accent, fontSize = 32.sp, fontWeight = FontWeight.Bold, fontFamily = font)
                    Spacer(Modifier.width(6.dp))
                    Text("days left", color = tokens.textPrimary, fontSize = 12.sp, modifier = Modifier.padding(bottom = 5.dp))
                }
                GlassProgressBar(progress = 0.68f, tokens = tokens, height = 5.dp)
            }
        }
        else -> {
            // Today Glass Card
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = resolveHorizontalAlignment(spec.alignment)
            ) {
                GlassBadgeChip(telemetry.weekdayFull.uppercase(), tokens, accented = true)
                Text("${telemetry.dayOfMonth}", color = tokens.textPrimary, fontSize = 38.sp, fontWeight = FontWeight.Bold, fontFamily = font, lineHeight = 38.sp)
                Text("${telemetry.monthFull} ${telemetry.year}", color = tokens.textSecondary, fontSize = 11.sp)
            }
        }
    }
}
