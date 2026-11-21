package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
import androidx.compose.material.icons.automirrored.rounded.VolumeOff
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.feedback.rememberGlassHaptics
import com.example.model.GlassTheme
import com.example.model.WidgetCategory
import com.example.model.WidgetInstanceConfig
import com.example.model.WidgetPresetRegistry
import com.example.model.WidgetPresetSpec
import com.example.model.WidgetSlotSize
import com.example.ui.renderers.LiveGlassWidgetPreview
import com.example.ui.theme.GlassFrostWhite
import com.example.ui.theme.GlassMidnight
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.LiquidCyan

@Composable
fun GlassGalleryScreen(
    viewModel: GlassGalleryViewModel,
    isWidgetConfigMode: Boolean = false,
    onApplyPlacedWidget: (() -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val telemetry by viewModel.telemetry.collectAsStateWithLifecycle()
    val haptics = rememberGlassHaptics()

    Box(modifier = Modifier.fillMaxSize()) {
        // Atmospheric Liquid Silk Aurora backdrop
        Image(
            painter = painterResource(id = R.drawable.img_glass_wallpaper_1795251470750),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            GlassMidnight.copy(alpha = 0.64f),
                            GlassMidnight.copy(alpha = 0.48f),
                            GlassMidnight.copy(alpha = 0.78f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Top Frosted Studio Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = if (isWidgetConfigMode) "Pick a Glass Widget" else "GlassWidgets",
                            style = MaterialTheme.typography.headlineMedium,
                            color = GlassFrostWhite
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(LiquidCyan.copy(alpha = 0.22f))
                                .border(1.dp, LiquidCyan.copy(alpha = 0.65f), RoundedCornerShape(50))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${uiState.totalPresetCount} Presets",
                                color = LiquidCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = JetBrainsMonoFontFamily
                            )
                        }
                    }
                    Text(
                        text = if (isWidgetConfigMode) {
                            "Tap any preset to customize & apply to your home screen"
                        } else {
                            "28 Categories · 4 Smart Slots · Liquid Glass"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = GlassFrostWhite.copy(alpha = 0.76f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    // Ambient Light Sound toggle button
                    IconButton(
                        onClick = {
                            haptics.click()
                            viewModel.toggleAmbientSound()
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                            .border(1.dp, Color.White.copy(alpha = 0.22f), CircleShape)
                            .testTag("ambient_sound_toggle")
                    ) {
                        Icon(
                            imageVector = if (uiState.isAmbientSoundEnabled) {
                                Icons.AutoMirrored.Rounded.VolumeUp
                            } else {
                                Icons.AutoMirrored.Rounded.VolumeOff
                            },
                            contentDescription = if (uiState.isAmbientSoundEnabled) "Mute ambient sound" else "Enable ambient sound",
                            tint = if (uiState.isAmbientSoundEnabled) LiquidCyan else GlassFrostWhite.copy(alpha = 0.65f)
                        )
                    }

                    // How to Add Widgets Quick Guide button
                    IconButton(
                        onClick = {
                            haptics.click()
                            viewModel.setHowToGuideVisible(true)
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                            .border(1.dp, Color.White.copy(alpha = 0.22f), CircleShape)
                            .testTag("how_to_add_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.HelpOutline,
                            contentDescription = "How to add widgets guide",
                            tint = GlassFrostWhite
                        )
                    }
                }
            }

            // Frosted Search Bar across all 840 presets
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                placeholder = {
                    Text(
                        "Search all 840 presets (Clock, AI, Weather, QR…)",
                        color = GlassFrostWhite.copy(alpha = 0.62f),
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingIcon = {
                    Icon(Icons.Rounded.Search, contentDescription = null, tint = LiquidCyan)
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            haptics.tick()
                            viewModel.onSearchQueryChanged("")
                        }) {
                            Icon(Icons.Rounded.Close, contentDescription = "Clear search", tint = GlassFrostWhite)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.14f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.10f),
                    focusedBorderColor = LiquidCyan.copy(alpha = 0.85f),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.22f),
                    focusedTextColor = GlassFrostWhite,
                    unfocusedTextColor = GlassFrostWhite
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("search_input")
            )

            Spacer(Modifier.height(10.dp))

            // Horizontal Scrollable Chip Bar for ALL 28 Categories (showing "30" badge count on each)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.testTag("category_chip_bar")
            ) {
                item(key = "all_categories") {
                    CategoryGlassChip(
                        label = "All",
                        badge = "${WidgetPresetRegistry.TOTAL_EXPECTED_PRESETS}",
                        selected = uiState.selectedCategory == null,
                        icon = Icons.Rounded.AutoAwesome,
                        onClick = {
                            haptics.tick()
                            viewModel.onCategorySelected(null)
                        },
                        testTag = "category_chip_all"
                    )
                }
                items(WidgetCategory.entries, key = { it.id }) { category ->
                    CategoryGlassChip(
                        label = category.displayName,
                        badge = "${WidgetPresetRegistry.PRESETS_PER_CATEGORY}",
                        selected = uiState.selectedCategory == category,
                        icon = categoryIcon(category),
                        onClick = {
                            haptics.tick()
                            viewModel.onCategorySelected(category)
                        },
                        testTag = "category_chip_${category.id}"
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Slot Size Filter Row (All / 2x2 / 4x1 / 4x2 / 4x4) + Result Count
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    SlotSizePill(
                        label = "All Sizes (${uiState.visiblePresets.size})",
                        selected = uiState.selectedSlotFilter == null,
                        onClick = {
                            haptics.tick()
                            viewModel.onSlotFilterSelected(null)
                        }
                    )
                }
                items(WidgetSlotSize.entries) { slot ->
                    SlotSizePill(
                        label = slot.label,
                        selected = uiState.selectedSlotFilter == slot,
                        onClick = {
                            haptics.tick()
                            viewModel.onSlotFilterSelected(slot)
                        }
                    )
                }
            }

            // Optional status confirmation banner (e.g., after pinning)
            AnimatedVisibility(
                visible = uiState.statusBannerMessage != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                uiState.statusBannerMessage?.let { msg ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(LiquidCyan.copy(alpha = 0.22f))
                            .border(1.dp, LiquidCyan.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(msg, color = GlassFrostWhite, fontSize = 12.sp, modifier = Modifier.weight(1f))
                        IconButton(onClick = viewModel::clearStatusBanner, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Rounded.Close, contentDescription = "Dismiss", tint = GlassFrostWhite, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(6.dp))

            // Live Jetpack Compose Widget Preview Grid
            if (uiState.visiblePresets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Rounded.Search, contentDescription = null, tint = LiquidCyan, modifier = Modifier.size(44.dp))
                        Text("No matching glass presets", style = MaterialTheme.typography.titleMedium, color = GlassFrostWhite)
                        Text(
                            "Try searching Clock, Weather, AI, Quotes, QR, or clear your filters.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GlassFrostWhite.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 162.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 6.dp,
                        bottom = 28.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .testTag("preset_preview_grid")
                ) {
                    items(uiState.visiblePresets, key = { it.id }) { spec ->
                        WidgetGalleryCard(
                            spec = spec,
                            telemetry = telemetry,
                            onClick = {
                                haptics.click()
                                viewModel.openCustomizerFor(spec)
                            }
                        )
                    }
                }
            }
        }

        // Customize Sheet
        uiState.activeCustomizeSpec?.let { spec ->
            WidgetCustomizeSheet(
                spec = spec,
                config = uiState.draftConfig,
                telemetry = telemetry,
                installedApps = uiState.installedApps,
                isConfiguringPlacedWidget = isWidgetConfigMode,
                onUpdateConfig = viewModel::updateDraft,
                onToggleApp = viewModel::toggleSelectedApp,
                onDismiss = viewModel::dismissCustomizer,
                onPrimaryAction = {
                    if (isWidgetConfigMode && onApplyPlacedWidget != null) {
                        onApplyPlacedWidget()
                    } else {
                        viewModel.requestPinSelectedWidget()
                    }
                }
            )
        }

        // How to Add Guide Sheet
        if (uiState.showHowToGuide) {
            HowToAddGuideSheet(onDismiss = { viewModel.setHowToGuideVisible(false) })
        }
    }
}

@Composable
private fun CategoryGlassChip(
    label: String,
    badge: String,
    selected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .height(40.dp)
            .clip(RoundedCornerShape(50))
            .background(if (selected) LiquidCyan.copy(alpha = 0.28f) else Color.White.copy(alpha = 0.11f))
            .border(
                width = 1.dp,
                color = if (selected) LiquidCyan else Color.White.copy(alpha = 0.22f),
                shape = RoundedCornerShape(50)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) LiquidCyan else GlassFrostWhite.copy(alpha = 0.85f),
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = label,
            color = GlassFrostWhite,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(if (selected) LiquidCyan else Color.White.copy(alpha = 0.18f))
                .padding(horizontal = 6.dp, vertical = 1.dp)
        ) {
            Text(
                text = badge,
                color = if (selected) GlassMidnight else GlassFrostWhite,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = JetBrainsMonoFontFamily
            )
        }
    }
}

@Composable
private fun SlotSizePill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) Color.White.copy(alpha = 0.24f) else Color.White.copy(alpha = 0.08f))
            .border(
                width = 0.8.dp,
                color = if (selected) Color.White.copy(alpha = 0.65f) else Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = GlassFrostWhite,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun WidgetGalleryCard(
    spec: WidgetPresetSpec,
    telemetry: com.example.data.WidgetTelemetrySnapshot,
    onClick: () -> Unit
) {
    val previewConfig = remember(spec.id) {
        WidgetInstanceConfig(
            presetId = spec.id,
            category = spec.category,
            glassTheme = spec.defaultTheme,
            opacity = when (spec.defaultTheme) {
                GlassTheme.LIGHT_FROSTED -> 0.56f
                GlassTheme.DARK_OBSIDIAN -> 0.48f
                else -> 0.42f
            },
            cornerRadiusDp = when (spec.indexInCategory % 3) {
                0 -> 24
                1 -> 18
                else -> 28
            },
            accentColorArgb = spec.defaultAccentArgb,
            textColorArgb = if (spec.defaultTheme == GlassTheme.LIGHT_FROSTED) 0xFF0F172AL else 0xFFFFFFFFL
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.Black.copy(alpha = 0.24f))
            .border(0.8.dp, Color.White.copy(alpha = 0.16f), RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .padding(10.dp)
            .testTag("widget_card_${spec.id}"),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LiveGlassWidgetPreview(
            spec = spec,
            config = previewConfig,
            telemetry = telemetry,
            modifier = Modifier
                .fillMaxWidth()
                .height(126.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = spec.name,
                    color = GlassFrostWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = spec.subtitle,
                    color = GlassFrostWhite.copy(alpha = 0.65f),
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.14f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = spec.recommendedSize.shortBadge,
                    color = LiquidCyan,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = JetBrainsMonoFontFamily
                )
            }
        }
    }
}
