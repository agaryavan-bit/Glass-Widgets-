package com.example.ui.renderers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AirplanemodeActive
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.Bluetooth
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Brightness6
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.FlashlightOn
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.ScreenRotation
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Storage
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.WidgetTelemetrySnapshot
import com.example.model.WidgetInstanceConfig
import com.example.model.WidgetPresetSpec
import com.example.render.GlassResolvedColors
import com.example.ui.theme.JetBrainsMonoFontFamily

// 5. QUICK SETTINGS RENDERER
@Composable
fun RenderQuickSettingsPreview(
    spec: WidgetPresetSpec,
    tokens: GlassResolvedColors
) {
    val tiles: List<Pair<ImageVector, String>> = when (spec.layoutVariant % 4) {
        0 -> listOf(Icons.Rounded.Wifi to "Wi-Fi", Icons.Rounded.Bluetooth to "BT", Icons.Rounded.VolumeUp to "Sound", Icons.Rounded.Brightness6 to "Light")
        1 -> listOf(Icons.Rounded.FlashlightOn to "Torch", Icons.Rounded.AirplanemodeActive to "Flight", Icons.Rounded.ScreenRotation to "Rotate", Icons.Rounded.Wifi to "Net")
        2 -> listOf(Icons.Rounded.Wifi to "Wi-Fi", Icons.Rounded.FlashlightOn to "Torch", Icons.Rounded.VolumeUp to "Audio")
        else -> listOf(Icons.Rounded.Brightness6 to "Bright", Icons.Rounded.ScreenRotation to "Auto", Icons.Rounded.Bluetooth to "BT", Icons.Rounded.AirplanemodeActive to "Flight")
    }

    if (spec.layoutVariant == 1 || spec.layoutVariant == 6) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(modifier = Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                tiles.take(2).forEachIndexed { idx, (icon, label) ->
                    QuickTilePod(icon, label, active = idx == 0, tokens = tokens, modifier = Modifier.weight(1f).fillMaxHeight())
                }
            }
            Row(modifier = Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                tiles.drop(2).take(2).forEachIndexed { idx, (icon, label) ->
                    QuickTilePod(icon, label, active = idx == 1, tokens = tokens, modifier = Modifier.weight(1f).fillMaxHeight())
                }
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            if (spec.layoutVariant == 5) {
                Text("QUICK CONTROLS", color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tiles.forEachIndexed { idx, (icon, label) ->
                    QuickTilePod(icon, label, active = idx == 0, tokens = tokens, modifier = Modifier.weight(1f).height(52.dp))
                }
            }
            if (spec.showProgress) {
                Spacer(Modifier.height(4.dp))
                GlassProgressBar(progress = 0.72f, tokens = tokens, height = 5.dp)
            }
        }
    }
}

@Composable
private fun QuickTilePod(
    icon: ImageVector,
    label: String,
    active: Boolean,
    tokens: GlassResolvedColors,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (active) tokens.accent.copy(alpha = 0.28f) else tokens.chipFill)
            .border(0.8.dp, if (active) tokens.accent.copy(alpha = 0.6f) else tokens.borderSubtle, RoundedCornerShape(14.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = label, tint = if (active) tokens.accent else tokens.textPrimary, modifier = Modifier.size(18.dp))
        Spacer(Modifier.height(2.dp))
        Text(label, color = tokens.textPrimary, fontSize = 9.sp, maxLines = 1)
    }
}

// 6. GOOGLE RENDERER
@Composable
fun RenderGooglePreview(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    tokens: GlassResolvedColors
) {
    val googleApps = listOf(
        Icons.Rounded.Search to "Search",
        Icons.Rounded.Mail to "Gmail",
        Icons.Rounded.Map to "Maps",
        Icons.Rounded.Cloud to "Drive",
        Icons.Rounded.CalendarMonth to "Cal",
        Icons.Rounded.PlayCircle to "YT",
        Icons.Rounded.PhotoLibrary to "Photos",
        Icons.Rounded.AutoAwesome to "Gemini"
    )
    when (spec.layoutVariant) {
        0, 8 -> {
            // Liquid Google Search Bar
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(50))
                    .background(tokens.chipFill)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("G", color = tokens.accent, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(
                        config.customText.ifBlank { "Search Google…" },
                        color = tokens.textSecondary,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Rounded.Mic, contentDescription = "Voice", tint = tokens.textPrimary, modifier = Modifier.size(16.dp))
                    Icon(Icons.Rounded.AutoAwesome, contentDescription = "Gemini", tint = tokens.accent, modifier = Modifier.size(16.dp))
                }
            }
        }
        1, 7 -> {
            // Workspace Quad / Hub
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(modifier = Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    googleApps.take(2).forEach { (ic, lbl) ->
                        QuickTilePod(ic, lbl, active = false, tokens = tokens, modifier = Modifier.weight(1f).fillMaxHeight())
                    }
                }
                Row(modifier = Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    googleApps.drop(2).take(2).forEach { (ic, lbl) ->
                        QuickTilePod(ic, lbl, active = lbl == "Drive", tokens = tokens, modifier = Modifier.weight(1f).fillMaxHeight())
                    }
                }
            }
        }
        else -> {
            // Explore Strip / Gemini + Search Dock
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlassBadgeChip("Google Suite", tokens, accented = true)
                    Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(14.dp))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    googleApps.drop(spec.layoutVariant % 4).take(4).forEachIndexed { idx, (ic, lbl) ->
                        QuickTilePod(ic, lbl, active = idx == 0, tokens = tokens, modifier = Modifier.weight(1f).height(48.dp))
                    }
                }
            }
        }
    }
}

// 7. PHOTO RENDERER
@Composable
fun RenderPhotoPreview(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    tokens: GlassResolvedColors
) {
    val caption = config.customText.ifBlank {
        when (spec.layoutVariant % 3) {
            0 -> "Alpine Twilight"
            1 -> "Golden Hour · 2026"
            else -> "Quiet Reflections"
        }
    }

    when (spec.layoutVariant) {
        1 -> {
            // Polaroid Memory
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                PhotoMediaBox(config.photoUri, Modifier.fillMaxWidth().weight(1f).clip(RoundedCornerShape(12.dp)))
                Spacer(Modifier.height(5.dp))
                Text(
                    caption,
                    color = tokens.textPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = resolveFontFamily(spec.typographyStyle),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        3, 5 -> {
            // Dual / Mosaic Collage
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                PhotoMediaBox(config.photoUri, Modifier.weight(1.2f).fillMaxHeight().clip(RoundedCornerShape(12.dp)))
                Column(modifier = Modifier.weight(1f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    PhotoMediaBox(config.photoUri, Modifier.weight(1f).fillMaxWidth().clip(RoundedCornerShape(10.dp)))
                    Box(
                        modifier = Modifier.weight(0.7f).fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(tokens.chipFill).padding(6.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(caption, color = tokens.textPrimary, fontSize = 9.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
        8 -> {
            // Circle Medallion
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                PhotoMediaBox(
                    config.photoUri,
                    Modifier.size(86.dp).clip(CircleShape).border(2.dp, tokens.borderHighlight, CircleShape)
                )
            }
        }
        else -> {
            // Full-bleed frosted glass frame + optional caption pill
            Box(modifier = Modifier.fillMaxSize()) {
                PhotoMediaBox(
                    config.photoUri,
                    Modifier.fillMaxSize().clip(RoundedCornerShape((tokens.cornerRadiusDp - 8).coerceAtLeast(8).dp))
                )
                if (spec.showIcon || spec.layoutVariant == 4 || spec.layoutVariant == 7) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(6.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color.Black.copy(alpha = 0.48f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(caption, color = Color.White, fontSize = 10.sp, maxLines = 1)
                    }
                }
            }
        }
    }
}

@Composable
private fun PhotoMediaBox(photoUri: String, modifier: Modifier) {
    if (photoUri.isNotBlank()) {
        AsyncImage(
            model = photoUri,
            contentDescription = "Selected photo",
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.img_sample_photo_1795251487510),
            contentDescription = "Sample glass frame photo",
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    }
}

// 8. WEATHER RENDERER (100% offline-safe cached/sample fallback)
@Composable
fun RenderWeatherPreview(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    tokens: GlassResolvedColors
) {
    val city = config.customText.ifBlank { "San Francisco" }
    val font = resolveFontFamily(spec.typographyStyle)

    when (spec.layoutVariant) {
        3, 4 -> {
            // Hourly / 5-Day Forecast Row
            val slots = if (spec.layoutVariant == 3) {
                listOf("12p" to "21°", "2p" to "23°", "4p" to "22°", "6p" to "19°", "8p" to "16°")
            } else {
                listOf("MON" to "22°", "TUE" to "24°", "WED" to "19°", "THU" to "21°", "FRI" to "23°")
            }
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(city, color = tokens.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Text("22° · Clear", color = tokens.accent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    slots.forEach { (label, temp) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(label, color = tokens.textSecondary, fontSize = 8.sp)
                            Icon(Icons.Rounded.WbSunny, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(14.dp))
                            Text(temp, color = tokens.textPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        5, 6 -> {
            // UV / Wind / Humidity telemetry
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                GlassBadgeChip(if (spec.layoutVariant == 5) "UV INDEX · 4 MOD" else "WIND · 12 KM/H", tokens, accented = true)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("HUMIDITY", color = tokens.textSecondary, fontSize = 8.sp)
                        Text("54%", color = tokens.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = font)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("DEW PT", color = tokens.textSecondary, fontSize = 8.sp)
                        Text("12°", color = tokens.accent, fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = font)
                    }
                }
                GlassProgressBar(progress = 0.54f, tokens = tokens, height = 5.dp)
            }
        }
        0, 2 -> {
            // Current Temp Pill / High-Low Strip
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Rounded.WbSunny, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(26.dp))
                    Column {
                        Text(city, color = tokens.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                        Text("Partly Clear · H:24° L:15°", color = tokens.textSecondary, fontSize = 9.sp)
                    }
                }
                Text("22°", color = tokens.textPrimary, fontSize = 28.sp, fontWeight = FontWeight.Bold, fontFamily = font)
            }
        }
        else -> {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(city, color = tokens.textSecondary, fontSize = 10.sp, maxLines = 1)
                    if (spec.showIcon) Icon(Icons.Rounded.WbSunny, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(18.dp))
                }
                Text("22°", color = tokens.textPrimary, fontSize = 36.sp, fontWeight = FontWeight.Bold, fontFamily = font, lineHeight = 36.sp)
                Text("Feels 21° · H 24° L 15°", color = tokens.textSecondary, fontSize = 10.sp)
            }
        }
    }
}

// 9. DEVICE RENDERER (real RAM, StatFs storage, CPU/uptime)
@Composable
fun RenderDevicePreview(
    spec: WidgetPresetSpec,
    telemetry: WidgetTelemetrySnapshot,
    tokens: GlassResolvedColors
) {
    val ramLabel = String.format("%.1f/%.0f GB", telemetry.ramUsedGb, telemetry.ramTotalGb)
    val diskLabel = String.format("%.0f/%.0f GB", telemetry.storageUsedGb, telemetry.storageTotalGb)

    when (spec.layoutVariant) {
        0, 8 -> {
            // RAM or Storage Donut
            val pct = if (spec.layoutVariant == 0) telemetry.ramPercent else telemetry.storagePercent
            val title = if (spec.layoutVariant == 0) "RAM" else "STORAGE"
            val sub = if (spec.layoutVariant == 0) ramLabel else diskLabel
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                GlassRingGauge(progress = pct, label = "${(pct * 100).toInt()}%", tokens = tokens, size = 56.dp)
                Column(horizontalAlignment = Alignment.End) {
                    Text(title, color = tokens.accent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(sub, color = tokens.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, fontFamily = JetBrainsMonoFontFamily)
                }
            }
        }
        3, 7 -> {
            // CPU & Uptime
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Icon(Icons.Rounded.Memory, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(15.dp))
                    Text("SYSTEM UPTIME", color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
                Text(telemetry.uptimeFormatted, color = tokens.textPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = JetBrainsMonoFontFamily)
                Text("${telemetry.cpuCores} Cores · ${telemetry.networkLabel}", color = tokens.accent, fontSize = 10.sp)
            }
        }
        else -> {
            // Combined RAM + StatFs Storage dashboard
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Rounded.Storage, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(13.dp))
                        Text("RAM", color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(ramLabel, color = tokens.textPrimary, fontSize = 10.sp, fontFamily = JetBrainsMonoFontFamily)
                }
                GlassProgressBar(progress = telemetry.ramPercent, tokens = tokens, height = 5.dp)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("STORAGE", color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(diskLabel, color = tokens.textPrimary, fontSize = 10.sp, fontFamily = JetBrainsMonoFontFamily)
                }
                GlassProgressBar(progress = telemetry.storagePercent, tokens = tokens, height = 5.dp)
            }
        }
    }
}

// 10. BATTERY RENDERER (real BatteryManager stats)
@Composable
fun RenderBatteryPreview(
    spec: WidgetPresetSpec,
    telemetry: WidgetTelemetrySnapshot,
    tokens: GlassResolvedColors
) {
    val pctFloat = telemetry.batteryPercent / 100f
    val font = resolveFontFamily(spec.typographyStyle)

    when (spec.layoutVariant) {
        0, 5 -> {
            // Percentage ring
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                GlassRingGauge(progress = pctFloat, label = "${telemetry.batteryPercent}%", tokens = tokens, size = 60.dp)
                Column(horizontalAlignment = Alignment.End) {
                    Text(if (telemetry.isCharging) "CHARGING" else "BATTERY", color = tokens.accent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(String.format("%.1f°C", telemetry.batteryTempC), color = tokens.textPrimary, fontSize = 12.sp, fontFamily = JetBrainsMonoFontFamily)
                    Text("${telemetry.batteryVoltageMv} mV", color = tokens.textSecondary, fontSize = 10.sp)
                }
            }
        }
        1, 2, 6 -> {
            // Horizontal bar / charging bolt pill / segmented bar
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(if (telemetry.isCharging) Icons.Rounded.Bolt else Icons.Rounded.BatteryChargingFull, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(16.dp))
                        Text(if (telemetry.isCharging) "Fast Charging" else "Power Reserve", color = tokens.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Text("${telemetry.batteryPercent}%", color = tokens.accent, fontSize = 15.sp, fontWeight = FontWeight.Bold, fontFamily = font)
                }
                Spacer(Modifier.height(6.dp))
                if (spec.layoutVariant == 6) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        val lit = (telemetry.batteryPercent / 10).coerceIn(1, 10)
                        for (i in 0 until 10) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(if (i < lit) tokens.accent else tokens.chipFill)
                            )
                        }
                    }
                } else {
                    GlassProgressBar(progress = pctFloat, tokens = tokens, height = 8.dp)
                }
            }
        }
        else -> {
            // Large bold readout / Thermal & voltage card
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    GlassBadgeChip(if (telemetry.isCharging) "CHARGING" else "HEALTHY", tokens, accented = true)
                    Text(String.format("%.1f°C", telemetry.batteryTempC), color = tokens.textSecondary, fontSize = 10.sp, fontFamily = JetBrainsMonoFontFamily)
                }
                Text("${telemetry.batteryPercent}%", color = tokens.textPrimary, fontSize = 36.sp, fontWeight = FontWeight.Bold, fontFamily = font, lineHeight = 36.sp)
                Text("${telemetry.batteryVoltageMv} mV · Li-Ion", color = tokens.textSecondary, fontSize = 10.sp)
            }
        }
    }
}
