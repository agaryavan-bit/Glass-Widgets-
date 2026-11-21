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
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Message
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.DeviceTelemetryProvider
import com.example.data.WidgetTelemetrySnapshot
import com.example.model.WidgetInstanceConfig
import com.example.model.WidgetInteractiveState
import com.example.model.WidgetPresetSpec
import com.example.render.GlassResolvedColors
import com.example.ui.theme.JetBrainsMonoFontFamily

// 11. CONTACTS RENDERER
@Composable
fun RenderContactsPreview(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    tokens: GlassResolvedColors
) {
    val people = listOf("AL" to "Alex", "MA" to "Maya", "NO" to "Noah", "SO" to "Sofia")
    when (spec.layoutVariant) {
        2, 3, 9 -> {
            // 1-Tap Call / Emergency ICE Card
            val title = config.customText.ifBlank { if (spec.layoutVariant == 3) "ICE · Emergency" else "Maya Lin" }
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier.size(40.dp).clip(CircleShape).background(tokens.accent.copy(alpha = 0.28f)).border(1.dp, tokens.accent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(if (spec.layoutVariant == 3) "SOS" else "ML", color = tokens.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text(title, color = tokens.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text("1-tap Call / SMS", color = tokens.textSecondary, fontSize = 9.sp)
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(tokens.accent.copy(alpha = 0.25f)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Rounded.Call, contentDescription = "Call", tint = tokens.accent, modifier = Modifier.size(16.dp))
                    }
                    Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(tokens.chipFill), contentAlignment = Alignment.Center) {
                        Icon(Icons.Rounded.Message, contentDescription = "Message", tint = tokens.textPrimary, modifier = Modifier.size(15.dp))
                    }
                }
            }
        }
        else -> {
            // 2x2 Speed dial / 4x1 Favorites Strip
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Text("FAVORITE CONTACTS", color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    people.forEachIndexed { idx, (initials, name) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(if (idx == 0) tokens.accent.copy(alpha = 0.30f) else tokens.chipFill)
                                    .border(1.dp, tokens.borderSubtle, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(initials, color = tokens.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(Modifier.height(2.dp))
                            Text(name, color = tokens.textSecondary, fontSize = 8.sp)
                        }
                    }
                }
            }
        }
    }
}

// 12. AI RENDERER (Gemini, ChatGPT, Claude, Grok, DeepSeek, Perplexity)
@Composable
fun RenderAiPreview(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    tokens: GlassResolvedColors
) {
    val models = listOf("Gemini", "ChatGPT", "Claude", "Grok", "DeepSeek", "Perplexity")
    when (spec.layoutVariant) {
        0, 3, 6 -> {
            // Prompt Bar Pill
            val promptLabel = config.customText.ifBlank {
                when (spec.layoutVariant) {
                    3 -> "Ask AI by voice…"
                    6 -> "Research with Perplexity…"
                    else -> "Ask Gemini anything…"
                }
            }
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
                    Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(18.dp))
                    Text(promptLabel, color = tokens.textPrimary, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                GlassBadgeChip("GO", tokens, accented = true)
            }
        }
        else -> {
            // Quartet / All-6 AI Hub
            val subset = if (spec.layoutVariant == 2 || spec.layoutVariant == 8) models else models.take(4)
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("AI LAUNCHPAD", color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(13.dp))
                }
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    subset.chunked(if (subset.size > 4) 3 else 2).forEach { rowItems ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            rowItems.forEachIndexed { idx, name ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (idx == 0) tokens.accent.copy(alpha = 0.24f) else tokens.chipFill)
                                        .padding(vertical = 6.dp, horizontal = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(name, color = tokens.textPrimary, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// 13. PRODUCTIVITY RENDERER
@Composable
fun RenderProductivityPreview(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    telemetry: WidgetTelemetrySnapshot,
    interactive: WidgetInteractiveState,
    tokens: GlassResolvedColors
) {
    val font = resolveFontFamily(spec.typographyStyle)
    when (spec.layoutVariant % 4) {
        0 -> {
            // Pomodoro mini-timer
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Icon(Icons.Rounded.Timer, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(15.dp))
                    GlassBadgeChip(if (interactive.pomodoroRunning) "ACTIVE" else "FOCUS", tokens, accented = true)
                }
                Text("${interactive.pomodoroMinutes}:00", color = tokens.textPrimary, fontSize = 30.sp, fontWeight = FontWeight.Bold, fontFamily = JetBrainsMonoFontFamily)
                GlassProgressBar(progress = interactive.pomodoroMinutes / 25f, tokens = tokens, height = 5.dp)
            }
        }
        1 -> {
            // Habit streak counter
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Rounded.LocalFireDepartment, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(16.dp))
                    Text(config.customText.ifBlank { "DAILY STREAK" }, color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                }
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("${interactive.habitStreak}", color = tokens.textPrimary, fontSize = 34.sp, fontWeight = FontWeight.Bold, fontFamily = font)
                    Spacer(Modifier.width(4.dp))
                    Text("days", color = tokens.accent, fontSize = 12.sp, modifier = Modifier.padding(bottom = 5.dp))
                }
                GlassProgressBar(progress = (interactive.habitStreak % 30) / 30f, tokens = tokens, height = 4.dp)
            }
        }
        2 -> {
            // Event / Exam Countdown
            val days = DeviceTelemetryProvider.getDaysUntil(config.targetDateEpochMillis)
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Text(config.customText.ifBlank { "EXAM COUNTDOWN" }, color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Text("T-$days Days", color = tokens.accent, fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = font)
                GlassProgressBar(progress = 0.65f, tokens = tokens, height = 5.dp)
            }
        }
        else -> {
            // Year progress bar / quick bookmarks
            val pct = (telemetry.yearProgress * 100).toInt()
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("YEAR ${telemetry.year}", color = tokens.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("$pct%", color = tokens.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = JetBrainsMonoFontFamily)
                }
                GlassProgressBar(progress = telemetry.yearProgress, tokens = tokens, height = 7.dp)
                Text("Focus · Notes · Docs", color = tokens.textSecondary, fontSize = 9.sp)
            }
        }
    }
}

// 14. NOTES / TO-DO RENDERER
@Composable
fun RenderNotesTodoPreview(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    interactive: WidgetInteractiveState,
    tokens: GlassResolvedColors
) {
    val items = DeviceTelemetryProvider.defaultChecklistItems
    when (spec.layoutVariant) {
        0, 3, 6, 8 -> {
            // Sticky glass note / pinned goal
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                GlassBadgeChip(if (spec.layoutVariant == 3) "PINNED GOAL" else "GLASS NOTE", tokens, accented = true)
                Text(
                    text = config.customText.ifBlank { "Ship clean Liquid Glass craft every day." },
                    color = tokens.textPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = resolveFontFamily(spec.typographyStyle),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Text("Tap to edit note", color = tokens.textSecondary, fontSize = 9.sp)
            }
        }
        else -> {
            // Interactive 3-5 item checklist
            val count = if (spec.layoutVariant == 4 || spec.layoutVariant == 9) 4 else 3
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Text("TO-DO LIST", color = tokens.accent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (i in 0 until count) {
                        val checked = ((interactive.checklistMask shr i) and 1) == 1
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(
                                imageVector = if (checked) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (checked) tokens.accent else tokens.textSecondary,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = items[i],
                                color = if (checked) tokens.textSecondary else tokens.textPrimary,
                                fontSize = 10.sp,
                                textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

// 15. APP LAUNCHER RENDERER
@Composable
fun RenderAppLauncherPreview(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    tokens: GlassResolvedColors
) {
    val defaultNames = listOf("Browser", "Camera", "Notes", "Music", "Mail", "Maps", "Clock", "Files")
    val appLabels = if (config.selectedApps.isNotEmpty()) {
        config.selectedApps.map { it.substringAfterLast('.').replaceFirstChar { c -> c.uppercase() } }
    } else {
        defaultNames
    }

    val count = when (spec.layoutVariant) {
        3 -> 1
        8 -> 2
        else -> 4
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        if (spec.showIcon) {
            Text("APP DOCK", color = tokens.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
        Row(modifier = Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
            appLabels.take(count).forEachIndexed { idx, name ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (idx == 0) tokens.accent.copy(alpha = 0.25f) else tokens.chipFill)
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Rounded.Apps, contentDescription = name, tint = if (idx == 0) tokens.accent else tokens.textPrimary, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.height(3.dp))
                    Text(name, color = tokens.textPrimary, fontSize = 9.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

// 16. FOLDER APPS RENDERER (4-app & 9-app glass folders for Study, Work, Media, Utilities)
@Composable
fun RenderFolderAppsPreview(
    spec: WidgetPresetSpec,
    tokens: GlassResolvedColors
) {
    val (folderTitle, items) = when (spec.layoutVariant % 4) {
        0 -> "Study" to listOf("Docs", "Anki", "Calc", "Notes", "PDF", "Timer", "Wiki", "Lib", "Lab")
        1 -> "Work" to listOf("Mail", "Cal", "Slack", "Meet", "Drive", "Tasks", "Jira", "Git", "Zoom")
        2 -> "Media" to listOf("Music", "Video", "Photos", "Cast", "Edit", "Radio", "Pods", "TV", "Art")
        else -> "Utilities" to listOf("Files", "Clock", "Scan", "VPN", "Speed", "Backup", "Pass", "Unit", "Tools")
    }
    val showNine = spec.layoutVariant == 1 || spec.layoutVariant == 3 || spec.layoutVariant == 8
    val cols = if (showNine) 3 else 2
    val rows = if (showNine) 3 else 2

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(Icons.Rounded.Folder, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(13.dp))
            Text(folderTitle.uppercase(), color = tokens.accent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
        Column(modifier = Modifier.weight(1f).padding(top = 4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            for (r in 0 until rows) {
                Row(modifier = Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (c in 0 until cols) {
                        val label = items[r * cols + c]
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(8.dp))
                                .background(tokens.chipFill),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(label, color = tokens.textPrimary, fontSize = if (showNine) 8.sp else 9.sp, maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}

// 17. MUSIC RENDERER
@Composable
fun RenderMusicPreview(
    spec: WidgetPresetSpec,
    interactive: WidgetInteractiveState,
    tokens: GlassResolvedColors
) {
    val track = DeviceTelemetryProvider.curatedTracks[interactive.musicTrackIndex % DeviceTelemetryProvider.curatedTracks.size]
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_sample_album_1795251623321),
            contentDescription = "Album artwork",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(if (spec.layoutVariant == 1 || spec.layoutVariant == 7) 44.dp else 58.dp)
                .clip(if (spec.layoutVariant == 3) CircleShape else RoundedCornerShape(12.dp))
                .border(1.dp, tokens.borderHighlight, if (spec.layoutVariant == 3) CircleShape else RoundedCornerShape(12.dp))
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.SpaceBetween) {
            Text(track.title, color = tokens.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(track.artist, color = tokens.textSecondary, fontSize = 10.sp, maxLines = 1)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Rounded.SkipPrevious, contentDescription = "Previous", tint = tokens.textSecondary, modifier = Modifier.size(16.dp))
                Box(
                    modifier = Modifier.size(24.dp).clip(CircleShape).background(tokens.accent.copy(alpha = 0.28f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (interactive.musicPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = tokens.accent,
                        modifier = Modifier.size(15.dp)
                    )
                }
                Icon(Icons.Rounded.SkipNext, contentDescription = "Next", tint = tokens.textSecondary, modifier = Modifier.size(16.dp))
            }
            if (spec.showProgress) {
                Spacer(Modifier.height(4.dp))
                GlassProgressBar(progress = 0.58f, tokens = tokens, height = 3.dp)
            }
        }
    }
}

// 18. HEALTH RENDERER
@Composable
fun RenderHealthPreview(
    spec: WidgetPresetSpec,
    interactive: WidgetInteractiveState,
    tokens: GlassResolvedColors
) {
    when (spec.layoutVariant % 3) {
        1 -> {
            // Water Intake Tap Counter
            val goal = 8
            val pct = (interactive.waterCount.toFloat() / goal).coerceIn(0f, 1f)
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.WaterDrop, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(16.dp))
                    GlassBadgeChip("+1 CUP", tokens, accented = true)
                }
                Text("${interactive.waterCount} / $goal cups", color = tokens.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                GlassProgressBar(progress = pct, tokens = tokens, height = 6.dp)
            }
        }
        2 -> {
            // Sleep / Breathing Wellness
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                GlassBadgeChip(if (spec.layoutVariant == 3) "BREATHE 4-7-8" else "SLEEP · 7h 45m", tokens, accented = true)
                Text(if (spec.layoutVariant == 3) "Inhale slowly · 4s" else "92% Recovery", color = tokens.textPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                GlassProgressBar(progress = 0.86f, tokens = tokens, height = 5.dp)
            }
        }
        else -> {
            // Daily Step Goal Ring
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                GlassRingGauge(progress = 0.74f, label = "74%", tokens = tokens, size = 58.dp)
                Column(horizontalAlignment = Alignment.End) {
                    Icon(Icons.Rounded.DirectionsWalk, contentDescription = null, tint = tokens.accent, modifier = Modifier.size(16.dp))
                    Text("7,420", color = tokens.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("/ 10k steps", color = tokens.textSecondary, fontSize = 9.sp)
                }
            }
        }
    }
}
