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
import com.example.data.DeviceTelemetryProvider
import com.example.data.WidgetTelemetrySnapshot
import com.example.model.WidgetInstanceConfig
import com.example.model.WidgetInteractiveState
import com.example.model.WidgetPresetSpec
import com.example.model.WidgetSlotSize
import com.example.render.GlanceBitmapPainter
import com.example.render.GlassResolvedColors

@Composable
fun GlanceContacts(
    config: WidgetInstanceConfig,
    tokens: GlassResolvedColors
) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))
    val contacts = listOf("Alex" to "5550101", "Maya" to "5550102", "Noah" to "5550103", "ICE" to "911")
    Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Text(config.customText.ifBlank { "SPEED DIAL" }, style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
        Spacer(GlanceModifier.height(6.dp))
        Row(modifier = GlanceModifier.fillMaxWidth()) {
            contacts.forEach { (name, num) ->
                Column(
                    modifier = GlanceModifier
                        .defaultWeight()
                        .padding(3.dp)
                        .background(Color(tokens.chipFillArgb))
                        .clickable(
                            actionRunCallback<LaunchShortcutAction>(
                                actionParametersOf(PARAM_ACTION_TYPE to "dial", PARAM_PAYLOAD to num)
                            )
                        )
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(name, style = TextStyle(color = primary, fontSize = 11.sp, fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Composable
fun GlanceAi(
    tokens: GlassResolvedColors
) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))
    val aiLinks = listOf(
        "Gemini" to "https://gemini.google.com",
        "ChatGPT" to "https://chatgpt.com",
        "Claude" to "https://claude.ai",
        "Perplexity" to "https://www.perplexity.ai"
    )
    Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Text("✦ AI HUB", style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
        Spacer(GlanceModifier.height(6.dp))
        Row(modifier = GlanceModifier.fillMaxWidth()) {
            aiLinks.forEach { (name, url) ->
                Column(
                    modifier = GlanceModifier
                        .defaultWeight()
                        .padding(2.dp)
                        .background(Color(tokens.chipFillArgb))
                        .clickable(
                            actionRunCallback<LaunchShortcutAction>(
                                actionParametersOf(PARAM_ACTION_TYPE to "url", PARAM_PAYLOAD to url)
                            )
                        )
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(name, style = TextStyle(color = primary, fontSize = 10.sp, fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Composable
fun GlanceProductivity(
    appWidgetId: Int,
    slotSize: WidgetSlotSize,
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    telemetry: WidgetTelemetrySnapshot,
    interactive: WidgetInteractiveState,
    tokens: GlassResolvedColors
) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val secondary = ColorProvider(Color(tokens.textSecondaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))

    when (spec.layoutVariant % 3) {
        0 -> Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .clickable(
                    actionRunCallback<InteractiveWidgetAction>(
                        actionParametersOf(
                            PARAM_WIDGET_ID to appWidgetId,
                            PARAM_SLOT_ID to slotSize.id,
                            PARAM_ACTION_TYPE to "pomo_toggle"
                        )
                    )
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(if (interactive.pomodoroRunning) "POMODORO · ACTIVE" else "POMODORO · TAP", style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
            Text("${interactive.pomodoroMinutes}:00", style = TextStyle(color = primary, fontSize = 30.sp, fontWeight = FontWeight.Bold))
            Text("Tap to step focus timer", style = TextStyle(color = secondary, fontSize = 10.sp))
        }
        1 -> Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .clickable(
                    actionRunCallback<InteractiveWidgetAction>(
                        actionParametersOf(
                            PARAM_WIDGET_ID to appWidgetId,
                            PARAM_SLOT_ID to slotSize.id,
                            PARAM_ACTION_TYPE to "habit_inc"
                        )
                    )
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(config.customText.ifBlank { "HABIT STREAK · TAP +1" }, style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
            Text("🔥 ${interactive.habitStreak} Days", style = TextStyle(color = primary, fontSize = 26.sp, fontWeight = FontWeight.Bold))
        }
        else -> {
            val days = DeviceTelemetryProvider.getDaysUntil(config.targetDateEpochMillis)
            Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Text(config.customText.ifBlank { "COUNTDOWN · ${telemetry.year}" }, style = TextStyle(color = secondary, fontSize = 10.sp))
                Text("T-$days Days", style = TextStyle(color = accent, fontSize = 26.sp, fontWeight = FontWeight.Bold))
            }
        }
    }
}

@Composable
fun GlanceNotesTodo(
    appWidgetId: Int,
    slotSize: WidgetSlotSize,
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    interactive: WidgetInteractiveState,
    tokens: GlassResolvedColors
) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val secondary = ColorProvider(Color(tokens.textSecondaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))

    if (spec.layoutVariant == 0 || spec.layoutVariant == 3) {
        Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Text("GLASS NOTE", style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
            Spacer(GlanceModifier.height(4.dp))
            Text(config.customText.ifBlank { "Craft calm, clear work today." }, style = TextStyle(color = primary, fontSize = 14.sp, fontWeight = FontWeight.Medium))
        }
    } else {
        val items = DeviceTelemetryProvider.defaultChecklistItems.take(3)
        Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Text("CHECKLIST · TAP TO TOGGLE", style = TextStyle(color = accent, fontSize = 9.sp, fontWeight = FontWeight.Bold))
            Spacer(GlanceModifier.height(4.dp))
            items.forEachIndexed { idx, label ->
                val done = ((interactive.checklistMask shr idx) and 1) == 1
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clickable(
                            actionRunCallback<InteractiveWidgetAction>(
                                actionParametersOf(
                                    PARAM_WIDGET_ID to appWidgetId,
                                    PARAM_SLOT_ID to slotSize.id,
                                    PARAM_ACTION_TYPE to "toggle_check",
                                    PARAM_INDEX to idx
                                )
                            )
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(if (done) "☑" else "☐", style = TextStyle(color = if (done) accent else primary, fontSize = 13.sp))
                    Spacer(GlanceModifier.width(6.dp))
                    Text(label, style = TextStyle(color = if (done) secondary else primary, fontSize = 11.sp))
                }
            }
        }
    }
}

@Composable
fun GlanceAppLauncher(
    config: WidgetInstanceConfig,
    tokens: GlassResolvedColors
) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))
    val apps = config.selectedApps.ifEmpty { listOf("com.android.settings", "com.android.chrome") }
    Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Text("APP DOCK", style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
        Spacer(GlanceModifier.height(6.dp))
        Row(modifier = GlanceModifier.fillMaxWidth()) {
            apps.take(4).forEach { pkg ->
                val shortName = pkg.substringAfterLast('.').replaceFirstChar { it.uppercase() }.take(8)
                Column(
                    modifier = GlanceModifier
                        .defaultWeight()
                        .padding(3.dp)
                        .background(Color(tokens.chipFillArgb))
                        .clickable(
                            actionRunCallback<LaunchShortcutAction>(
                                actionParametersOf(PARAM_ACTION_TYPE to "app", PARAM_PAYLOAD to pkg)
                            )
                        )
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(shortName, style = TextStyle(color = primary, fontSize = 10.sp, fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Composable
fun GlanceFolderApps(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    tokens: GlassResolvedColors
) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))
    val title = when (spec.layoutVariant % 4) {
        0 -> "STUDY FOLDER"
        1 -> "WORK FOLDER"
        2 -> "MEDIA FOLDER"
        else -> "UTILITIES FOLDER"
    }
    val labels = if (config.selectedApps.isNotEmpty()) {
        config.selectedApps.take(4).map { it.substringAfterLast('.').take(7) to it }
    } else {
        listOf("Settings" to "com.android.settings", "Browser" to "com.android.chrome", "Clock" to "com.google.android.deskclock", "Files" to "com.google.android.documentsui")
    }
    Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Text(title, style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
        Spacer(GlanceModifier.height(5.dp))
        Row(modifier = GlanceModifier.fillMaxWidth()) {
            labels.forEach { (name, pkg) ->
                Column(
                    modifier = GlanceModifier
                        .defaultWeight()
                        .padding(2.dp)
                        .background(Color(tokens.chipFillArgb))
                        .clickable(
                            actionRunCallback<LaunchShortcutAction>(
                                actionParametersOf(PARAM_ACTION_TYPE to "app", PARAM_PAYLOAD to pkg)
                            )
                        )
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(name, style = TextStyle(color = primary, fontSize = 10.sp, fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Composable
fun GlanceMusic(
    context: Context,
    appWidgetId: Int,
    slotSize: WidgetSlotSize,
    interactive: WidgetInteractiveState,
    tokens: GlassResolvedColors
) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val secondary = ColorProvider(Color(tokens.textSecondaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))
    val track = DeviceTelemetryProvider.curatedTracks[interactive.musicTrackIndex % DeviceTelemetryProvider.curatedTracks.size]
    val album = GlanceBitmapPainter.loadRoundedPhotoBitmap(context, "", R.drawable.img_sample_album_1795251623321, 140, 140, 24f)

    Row(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Image(provider = ImageProvider(album), contentDescription = "Album art", modifier = GlanceModifier.size(58.dp))
        Spacer(GlanceModifier.width(10.dp))
        Column(modifier = GlanceModifier.defaultWeight()) {
            Text(track.title, style = TextStyle(color = primary, fontSize = 13.sp, fontWeight = FontWeight.Bold))
            Text(track.artist, style = TextStyle(color = secondary, fontSize = 10.sp))
            Spacer(GlanceModifier.height(6.dp))
            Row {
                Text(
                    "⏮",
                    modifier = GlanceModifier.padding(end = 10.dp).clickable(
                        actionRunCallback<InteractiveWidgetAction>(
                            actionParametersOf(PARAM_WIDGET_ID to appWidgetId, PARAM_SLOT_ID to slotSize.id, PARAM_ACTION_TYPE to "music_prev")
                        )
                    ),
                    style = TextStyle(color = primary, fontSize = 15.sp)
                )
                Text(
                    if (interactive.musicPlaying) "⏸" else "▶",
                    modifier = GlanceModifier.padding(end = 10.dp).clickable(
                        actionRunCallback<InteractiveWidgetAction>(
                            actionParametersOf(PARAM_WIDGET_ID to appWidgetId, PARAM_SLOT_ID to slotSize.id, PARAM_ACTION_TYPE to "music_toggle")
                        )
                    ),
                    style = TextStyle(color = accent, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                )
                Text(
                    "⏭",
                    modifier = GlanceModifier.clickable(
                        actionRunCallback<InteractiveWidgetAction>(
                            actionParametersOf(PARAM_WIDGET_ID to appWidgetId, PARAM_SLOT_ID to slotSize.id, PARAM_ACTION_TYPE to "music_next")
                        )
                    ),
                    style = TextStyle(color = primary, fontSize = 15.sp)
                )
            }
        }
    }
}

@Composable
fun GlanceHealth(
    appWidgetId: Int,
    slotSize: WidgetSlotSize,
    spec: WidgetPresetSpec,
    interactive: WidgetInteractiveState,
    tokens: GlassResolvedColors
) {
    val primary = ColorProvider(Color(tokens.textPrimaryArgb))
    val secondary = ColorProvider(Color(tokens.textSecondaryArgb))
    val accent = ColorProvider(Color(tokens.accentArgb))

    if (spec.layoutVariant % 2 == 1) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .clickable(
                    actionRunCallback<InteractiveWidgetAction>(
                        actionParametersOf(PARAM_WIDGET_ID to appWidgetId, PARAM_SLOT_ID to slotSize.id, PARAM_ACTION_TYPE to "water_inc")
                    )
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("💧 WATER INTAKE · TAP +1", style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
            Text("${interactive.waterCount} / 8 Cups", style = TextStyle(color = primary, fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text("Stay hydrated today", style = TextStyle(color = secondary, fontSize = 10.sp))
        }
    } else {
        val ring = GlanceBitmapPainter.drawProgressRing(140, 0.74f, "74%", tokens)
        Row(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Image(provider = ImageProvider(ring), contentDescription = "Steps ring", modifier = GlanceModifier.size(60.dp))
            Spacer(GlanceModifier.width(10.dp))
            Column {
                Text("DAILY STEPS", style = TextStyle(color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold))
                Text("7,420", style = TextStyle(color = primary, fontSize = 22.sp, fontWeight = FontWeight.Bold))
                Text("Goal: 10,000", style = TextStyle(color = secondary, fontSize = 10.sp))
            }
        }
    }
}
