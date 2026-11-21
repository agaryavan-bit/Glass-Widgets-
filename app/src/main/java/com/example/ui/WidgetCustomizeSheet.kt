package com.example.ui

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.WidgetTelemetrySnapshot
import com.example.feedback.rememberGlassHaptics
import com.example.model.GlassTheme
import com.example.model.InstalledAppInfo
import com.example.model.WidgetCategory
import com.example.model.WidgetInstanceConfig
import com.example.model.WidgetPresetSpec
import com.example.ui.renderers.LiveGlassWidgetPreview
import com.example.ui.theme.CuratedAccentPalette
import com.example.ui.theme.CuratedTextPalette
import com.example.ui.theme.GlassDeepNavy
import com.example.ui.theme.GlassFrostWhite
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.LiquidCyan
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WidgetCustomizeSheet(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    telemetry: WidgetTelemetrySnapshot,
    installedApps: List<InstalledAppInfo>,
    isConfiguringPlacedWidget: Boolean,
    onUpdateConfig: ((WidgetInstanceConfig) -> WidgetInstanceConfig) -> Unit,
    onToggleApp: (String) -> Unit,
    onDismiss: () -> Unit,
    onPrimaryAction: () -> Unit
) {
    val context = LocalContext.current
    val haptics = rememberGlassHaptics()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Zero-permission Android Photo Picker for Photo category widgets
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            runCatching {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            haptics.confirm()
            onUpdateConfig { it.copy(photoUri = uri.toString()) }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = GlassDeepNavy.copy(alpha = 0.97f),
        contentColor = GlassFrostWhite,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = spec.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = GlassFrostWhite
                    )
                    Text(
                        text = "${spec.category.displayName} · Recommended ${spec.recommendedSize.label}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LiquidCyan
                    )
                }
                IconButton(
                    onClick = {
                        haptics.click()
                        onDismiss()
                    },
                    modifier = Modifier.testTag("customize_close_button")
                ) {
                    Icon(Icons.Rounded.Close, contentDescription = "Close customizer", tint = GlassFrostWhite)
                }
            }

            // Live Liquid Glass Preview Stage over aurora wallpaper
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(26.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_glass_wallpaper_1795251470750),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(if (spec.recommendedSize.cellCols == 2) 0.56f else 0.90f)
                        .aspectRatio(spec.recommendedSize.previewAspectRatio.coerceIn(1.0f, 2.3f))
                ) {
                    LiveGlassWidgetPreview(
                        spec = spec,
                        config = config,
                        telemetry = telemetry,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Glass Theme selector
            SectionLabel("Liquid Glass Theme")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(GlassTheme.entries) { theme ->
                    val selected = config.glassTheme == theme
                    FilterChip(
                        selected = selected,
                        onClick = {
                            haptics.tick()
                            val defaultText = if (theme == GlassTheme.LIGHT_FROSTED) 0xFF0F172AL else 0xFFFFFFFFL
                            onUpdateConfig { it.copy(glassTheme = theme, textColorArgb = defaultText) }
                        },
                        label = { Text(theme.label, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = LiquidCyan.copy(alpha = 0.25f),
                            selectedLabelColor = GlassFrostWhite,
                            containerColor = Color.White.copy(alpha = 0.08f),
                            labelColor = GlassFrostWhite.copy(alpha = 0.8f)
                        ),
                        modifier = Modifier.testTag("theme_chip_${theme.id}")
                    )
                }
            }

            // Opacity slider (10% to 90%)
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SectionLabel("Glass Opacity")
                    Text(
                        "${(config.opacity * 100).roundToInt()}%",
                        color = LiquidCyan,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                Slider(
                    value = config.opacity,
                    onValueChange = { value ->
                        val stepped = (value * 20).roundToInt() / 20f
                        if (stepped != config.opacity) haptics.tick()
                        onUpdateConfig { it.copy(opacity = value.coerceIn(0.10f, 0.90f)) }
                    },
                    valueRange = 0.10f..0.90f,
                    colors = SliderDefaults.colors(thumbColor = LiquidCyan, activeTrackColor = LiquidCyan),
                    modifier = Modifier.testTag("opacity_slider")
                )
            }

            // Corner radius slider (12dp to 32dp)
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SectionLabel("Corner Radius")
                    Text(
                        "${config.cornerRadiusDp} dp",
                        color = LiquidCyan,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                Slider(
                    value = config.cornerRadiusDp.toFloat(),
                    onValueChange = { value ->
                        val intVal = value.roundToInt().coerceIn(12, 32)
                        if (intVal != config.cornerRadiusDp) haptics.tick()
                        onUpdateConfig { it.copy(cornerRadiusDp = intVal) }
                    },
                    valueRange = 12f..32f,
                    colors = SliderDefaults.colors(thumbColor = LiquidCyan, activeTrackColor = LiquidCyan),
                    modifier = Modifier.testTag("corner_radius_slider")
                )
            }

            // Accent Color Swatches
            SectionLabel("Accent Tint")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(CuratedAccentPalette) { argb ->
                    val selected = config.accentColorArgb == argb
                    ColorOrb(
                        color = Color(argb),
                        selected = selected,
                        onClick = {
                            haptics.tick()
                            onUpdateConfig { it.copy(accentColorArgb = argb) }
                        }
                    )
                }
            }

            // Text Color Swatches (activates Custom theme cleanly when tweaked)
            SectionLabel("Text Color")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(CuratedTextPalette) { argb ->
                    val selected = config.textColorArgb == argb
                    ColorOrb(
                        color = Color(argb),
                        selected = selected,
                        onClick = {
                            haptics.tick()
                            onUpdateConfig {
                                it.copy(textColorArgb = argb, glassTheme = GlassTheme.CUSTOM)
                            }
                        }
                    )
                }
            }

            // Custom Text input (works across Notes, Quotes, Personalise QR/Banner, Weather City, Countdown title, Photo Caption)
            SectionLabel("Custom Label / Note / QR Text")
            OutlinedTextField(
                value = config.customText,
                onValueChange = { txt -> onUpdateConfig { it.copy(customText = txt.take(64)) } },
                placeholder = { Text("Optional caption, note, city, or QR text…", fontSize = 13.sp) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LiquidCyan,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
                    focusedTextColor = GlassFrostWhite,
                    unfocusedTextColor = GlassFrostWhite
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("custom_text_input")
            )

            // Contextual: Photo Picker for Photo category
            if (spec.category == WidgetCategory.PHOTO) {
                OutlinedButton(
                    onClick = {
                        haptics.click()
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("pick_photo_button")
                ) {
                    Icon(Icons.Rounded.PhotoLibrary, contentDescription = null, tint = LiquidCyan)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (config.photoUri.isBlank()) "Choose Photo from Gallery" else "Change Selected Photo",
                        color = GlassFrostWhite
                    )
                }
            }

            // Contextual: Target Date selector for Calendar / Productivity countdowns
            if (spec.category == WidgetCategory.CALENDAR || spec.category == WidgetCategory.PRODUCTIVITY) {
                SectionLabel("Target Countdown Date")
                val offsets = listOf(7 to "+7d", 14 to "+14d", 30 to "+30d", 90 to "+90d", 180 to "+180d")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(offsets) { (days, label) ->
                        FilterChip(
                            selected = false,
                            onClick = {
                                haptics.tick()
                                onUpdateConfig {
                                    it.copy(targetDateEpochMillis = System.currentTimeMillis() + days * 86_400_000L)
                                }
                            },
                            label = { Text(label) }
                        )
                    }
                }
            }

            // Contextual: Installed Apps selector for App Launcher / Folder Apps
            if ((spec.category == WidgetCategory.APP_LAUNCHER || spec.category == WidgetCategory.FOLDER_APPS) && installedApps.isNotEmpty()) {
                SectionLabel("Choose Apps (${config.selectedApps.size} selected)")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 2.dp)
                ) {
                    items(installedApps.take(24), key = { it.packageName }) { app ->
                        val picked = config.selectedApps.contains(app.packageName)
                        FilterChip(
                            selected = picked,
                            onClick = {
                                haptics.tick()
                                onToggleApp(app.packageName)
                            },
                            label = { Text(app.label, maxLines = 1, fontSize = 12.sp) }
                        )
                    }
                }
            }

            // Primary Apply / Pin button
            Button(
                onClick = {
                    haptics.confirm()
                    onPrimaryAction()
                },
                colors = ButtonDefaults.buttonColors(containerColor = LiquidCyan, contentColor = GlassDeepNavy),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("apply_widget_button")
            ) {
                Icon(
                    imageVector = if (isConfiguringPlacedWidget) Icons.Rounded.Check else Icons.Rounded.PushPin,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (isConfiguringPlacedWidget) "Apply to Home Screen Widget" else "Pin ${spec.recommendedSize.label} to Home Screen",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        color = GlassFrostWhite.copy(alpha = 0.72f),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.8.sp
    )
}

@Composable
private fun ColorOrb(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(if (selected) 36.dp else 30.dp)
                .clip(CircleShape)
                .background(color)
                .border(
                    width = if (selected) 2.5.dp else 1.dp,
                    color = if (selected) Color.White else Color.White.copy(alpha = 0.4f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = "Selected color",
                    tint = if (color == Color.White || color.red > 0.8f && color.green > 0.8f) Color.Black else Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
