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
import androidx.compose.material.icons.rounded.BluetoothAudio
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Casino
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material.icons.rounded.Headphones
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.PhotoCameraFront
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DeviceTelemetryProvider
import com.example.model.WidgetInstanceConfig
import com.example.model.WidgetInteractiveState
import com.example.model.WidgetPresetSpec
import com.example.render.GlassResolvedColors
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.PlayfairDisplayFontFamily

// 19. FINANCE RENDERER (Crypto, FX, Gold/Silver, Market Indices — offline-safe cached sample)
@Composable
fun RenderFinancePreview(
    spec: WidgetPresetSpec,
    tokens: GlassResolvedColors
) {
    val rows: List<Triple<String, String, String>> = when (spec.layoutVariant % 4) {
        0 -> listOf(Triple("BTC", "$94,280", "+2.4%"), Triple("ETH", "$3,410", "+1.8%"))
        1 -> listOf(Triple("EUR/USD", "1.084", "+0.3%"), Triple("USD/JPY", "151.2", "-0.2%"))
        2 -> listOf(Triple("GOLD", "$2,684", "+0.9%"), Triple("SILVER", "$31.40", "+1.2%"))
        else -> listOf(Triple("S&P 500", "5,982", "+0.6%"), Triple("NASDAQ", "19,140", "+1.1%"))
    }
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("MARKETS", color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Icon(Icons.Rounded.TrendingUp, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(14.dp))
        }
        rows.forEach { (sym, price, delta) ->
            Row(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(tokens.chipFill).padding(horizontal = 7.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(sym, color = tokens.textPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(price, color = tokens.textPrimary, fontSize = 11.sp, fontFamily = JetBrainsMonoFontFamily, fontWeight = FontWeight.SemiBold)
                Text(delta, color = tokens.accent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 20. BLUETOOTH RENDERER
@Composable
fun RenderBluetoothPreview(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    tokens: GlassResolvedColors
) {
    val gearName = config.customText.ifBlank { if (spec.layoutVariant % 2 == 0) "AirBuds Pro" else "Studio WH-1000" }
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Icon(
                    imageVector = if (spec.layoutVariant % 2 == 0) Icons.Rounded.BluetoothAudio else Icons.Rounded.Headphones,
                    contentDescription = null,
                    tint = tokens.accent,
                    modifier = Modifier.size(16.dp)
                )
                Text(gearName, color = tokens.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            }
            GlassBadgeChip("LDAC", tokens, accented = true)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text("LEFT", color = tokens.textSecondary, fontSize = 8.sp)
                Text("92%", color = tokens.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = JetBrainsMonoFontFamily)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("RIGHT", color = tokens.textSecondary, fontSize = 8.sp)
                Text("88%", color = tokens.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = JetBrainsMonoFontFamily)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("CASE", color = tokens.textSecondary, fontSize = 8.sp)
                Text("76%", color = tokens.accent, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = JetBrainsMonoFontFamily)
            }
        }
        GlassProgressBar(progress = 0.88f, tokens = tokens, height = 4.dp)
    }
}

// 21. SOCIAL RENDERER
@Composable
fun RenderSocialPreview(
    spec: WidgetPresetSpec,
    tokens: GlassResolvedColors
) {
    val networks = listOf("WA" to "WhatsApp", "IG" to "Instagram", "X" to "X", "RD" to "Reddit", "TG" to "Telegram", "DC" to "Discord")
    val slice = if (spec.layoutVariant == 1 || spec.layoutVariant == 5) networks else networks.take(4)

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Text("SOCIAL HUB", color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            slice.take(4).forEachIndexed { idx, (code, name) ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (idx == 0) tokens.accent.copy(alpha = 0.25f) else tokens.chipFill)
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(code, color = if (idx == 0) tokens.accent else tokens.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(name.take(4), color = tokens.textSecondary, fontSize = 8.sp, maxLines = 1)
                }
            }
        }
    }
}

// 22. GAMES RENDERER (Dice, Coin Flip, Tap Counter, Tic-Tac-Toe)
@Composable
fun RenderGamesPreview(
    spec: WidgetPresetSpec,
    interactive: WidgetInteractiveState,
    tokens: GlassResolvedColors
) {
    when (spec.layoutVariant % 4) {
        0 -> {
            // Dice Roller
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Rounded.Casino, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(14.dp))
                    Text("GLASS DIE", color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(tokens.accent.copy(alpha = 0.26f)).border(1.dp, tokens.accent, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("${interactive.diceValue}", color = tokens.textPrimary, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                }
                Text("Tap to roll", color = tokens.textSecondary, fontSize = 9.sp)
            }
        }
        1 -> {
            // Coin Flip
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally) {
                Text("COIN FLIP", color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier.size(50.dp).clip(CircleShape).background(tokens.accent.copy(alpha = 0.25f)).border(1.5.dp, tokens.accent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (interactive.coinHeads) "H" else "T", color = tokens.textPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Text(if (interactive.coinHeads) "HEADS" else "TAILS", color = tokens.accent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
        3 -> {
            // Tic-Tac-Toe Mini Grid
            val board = interactive.tttBoard.padEnd(9, '_')
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                for (r in 0 until 3) {
                    Row(modifier = Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        for (c in 0 until 3) {
                            val ch = board[r * 3 + c]
                            Box(
                                modifier = Modifier.weight(1f).fillMaxSize().clip(RoundedCornerShape(6.dp)).background(tokens.chipFill),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (ch == '_') "" else ch.toString(),
                                    color = if (ch == 'X') tokens.accent else tokens.textPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
        else -> {
            // Tap Counter Clicker
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally) {
                Text("TAP COUNTER", color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text("${interactive.tapCounter}", color = tokens.accent, fontSize = 32.sp, fontWeight = FontWeight.Bold, fontFamily = JetBrainsMonoFontFamily)
                GlassBadgeChip("+1 TAP", tokens, accented = true)
            }
        }
    }
}

// 23. CAMERA RENDERER
@Composable
fun RenderCameraPreview(
    spec: WidgetPresetSpec,
    tokens: GlassResolvedColors
) {
    val modes = listOf(
        Icons.Rounded.CameraAlt to "Photo",
        Icons.Rounded.Videocam to "Video",
        Icons.Rounded.PhotoCameraFront to "Selfie",
        Icons.Rounded.QrCodeScanner to "QR"
    )
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Text("LENS SHORTCUTS", color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            modes.forEachIndexed { idx, (icon, label) ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (idx == spec.layoutVariant % 4) tokens.accent.copy(alpha = 0.28f) else tokens.chipFill)
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(icon, contentDescription = label, tint = if (idx == spec.layoutVariant % 4) tokens.accent else tokens.textPrimary, modifier = Modifier.size(17.dp))
                    Spacer(Modifier.height(2.dp))
                    Text(label, color = tokens.textPrimary, fontSize = 8.sp)
                }
            }
        }
    }
}

// 24. QUOTES RENDERER
@Composable
fun RenderQuotesPreview(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    interactive: WidgetInteractiveState,
    tokens: GlassResolvedColors
) {
    val quote = DeviceTelemetryProvider.curatedQuotes[(interactive.quoteIndex + spec.layoutVariant) % DeviceTelemetryProvider.curatedQuotes.size]
    val displayQuote = config.customText.ifBlank { quote.text }
    val font = if (spec.layoutVariant == 0 || spec.layoutVariant == 6) PlayfairDisplayFontFamily else resolveFontFamily(spec.typographyStyle)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = resolveHorizontalAlignment(spec.alignment)
    ) {
        if (spec.showIcon) {
            Icon(Icons.Rounded.FormatQuote, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(16.dp))
        }
        Text(
            text = "“$displayQuote”",
            color = tokens.textPrimary,
            fontSize = 12.sp,
            fontStyle = if (font == PlayfairDisplayFontFamily) FontStyle.Italic else FontStyle.Normal,
            fontFamily = font,
            lineHeight = 16.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Text("— ${quote.author}", color = tokens.accent, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
    }
}

// 25. RELIGIOUS RENDERER
@Composable
fun RenderReligiousPreview(
    spec: WidgetPresetSpec,
    tokens: GlassResolvedColors
) {
    val schedule = DeviceTelemetryProvider.prayerSchedule
    when (spec.layoutVariant % 3) {
        1 -> {
            // Daily Prayer Times Schedule
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Rounded.NightsStay, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(13.dp))
                    Text("PRAYER SCHEDULE", color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    schedule.forEachIndexed { idx, slot ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(slot.name.take(3), color = if (idx == 2) tokens.accent else tokens.textSecondary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            Text(slot.time, color = tokens.textPrimary, fontSize = 9.sp, fontFamily = JetBrainsMonoFontFamily)
                        }
                    }
                }
            }
        }
        2 -> {
            // Reflection Verse Card
            val verse = DeviceTelemetryProvider.reflectionVerses[spec.layoutVariant % DeviceTelemetryProvider.reflectionVerses.size]
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                GlassBadgeChip("DAILY REFLECTION", tokens, accented = true)
                Text(verse, color = tokens.textPrimary, fontSize = 12.sp, fontFamily = PlayfairDisplayFontFamily, maxLines = 3)
                Text("Peace & Gratitude", color = tokens.textSecondary, fontSize = 9.sp)
            }
        }
        else -> {
            // Next Prayer Countdown
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("NEXT · ASR", color = tokens.accent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.Rounded.NightsStay, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(14.dp))
                }
                Text("15:46", color = tokens.textPrimary, fontSize = 26.sp, fontWeight = FontWeight.Bold, fontFamily = JetBrainsMonoFontFamily)
                Text("In 1h 24m", color = tokens.textSecondary, fontSize = 10.sp)
            }
        }
    }
}

// 26. COMPASS RENDERER
@Composable
fun RenderCompassPreview(
    spec: WidgetPresetSpec,
    tokens: GlassResolvedColors
) {
    val heading = (32 + spec.layoutVariant * 37) % 360
    val cardinal = when {
        heading < 45 || heading >= 315 -> "N"
        heading < 135 -> "E"
        heading < 225 -> "S"
        else -> "W"
    }
    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        GlassCompassCanvas(headingDeg = heading, tokens = tokens, size = 62.dp)
        Column(horizontalAlignment = Alignment.End) {
            Text("HEADING", color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text("${heading}° $cardinal", color = tokens.textPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = JetBrainsMonoFontFamily)
            GlassBadgeChip("MAGNETIC", tokens, accented = true)
        }
    }
}

// 27. CALCULATOR RENDERER
@Composable
fun RenderCalculatorPreview(
    spec: WidgetPresetSpec,
    interactive: WidgetInteractiveState,
    tokens: GlassResolvedColors
) {
    when (spec.layoutVariant) {
        2, 4, 7 -> {
            // Tip / Percentage shortcut
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("TIP & PERCENT", color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.Rounded.Calculate, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(14.dp))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    listOf("15%", "18%", "20%", "25%").forEachIndexed { idx, pct ->
                        Box(
                            modifier = Modifier.weight(1f).clip(RoundedCornerShape(10.dp)).background(if (idx == 1) tokens.accent.copy(alpha = 0.28f) else tokens.chipFill).padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(pct, color = tokens.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        else -> {
            // Interactive Keypad Preview
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Box(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(tokens.chipFill).padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(interactive.calcDisplay, color = tokens.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = JetBrainsMonoFontFamily)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("7", "8", "9", "÷").forEachIndexed { i, k ->
                        Box(
                            modifier = Modifier.weight(1f).clip(RoundedCornerShape(7.dp)).background(if (i == 3) tokens.accent.copy(alpha = 0.28f) else tokens.chipFill).padding(vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) { Text(k, color = tokens.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("4", "5", "6", "=").forEachIndexed { i, k ->
                        Box(
                            modifier = Modifier.weight(1f).clip(RoundedCornerShape(7.dp)).background(if (i == 3) tokens.accent.copy(alpha = 0.32f) else tokens.chipFill).padding(vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) { Text(k, color = tokens.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }
    }
}

// 28. PERSONALISE RENDERER (Typography Banner, Text-to-QR, Aesthetic Glass Spacer/Badge)
@Composable
fun RenderPersonalisePreview(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    tokens: GlassResolvedColors
) {
    val text = config.customText.ifBlank { "Liquid Glass" }
    val font = resolveFontFamily(spec.typographyStyle)
    when (spec.layoutVariant) {
        1, 4 -> {
            // Custom Text-to-QR widget
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                GlassQrCodeCanvas(payload = text, tokens = tokens, size = 66.dp)
                Spacer(Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    GlassBadgeChip("QR CODE", tokens, accented = true)
                    Spacer(Modifier.height(4.dp))
                    Text(text, color = tokens.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, textAlign = TextAlign.End)
                }
            }
        }
        2, 9 -> {
            // Pure Aesthetic Glass Spacer
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("✦", color = tokens.borderHighlight, fontSize = 22.sp)
            }
        }
        else -> {
            // Custom Typography Banner / Monogram Badge
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = resolveHorizontalAlignment(spec.alignment)
            ) {
                if (spec.showIcon) {
                    GlassBadgeChip("CUSTOM BADGE", tokens, accented = true)
                    Spacer(Modifier.height(5.dp))
                }
                Text(
                    text = text,
                    color = tokens.textPrimary,
                    fontSize = 22.sp,
                    fontWeight = resolveFontWeight(spec.typographyStyle),
                    fontFamily = font,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
