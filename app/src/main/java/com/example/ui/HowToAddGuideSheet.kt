package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedback.rememberGlassHaptics
import com.example.ui.theme.GlassDeepNavy
import com.example.ui.theme.GlassFrostWhite
import com.example.ui.theme.LiquidCyan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HowToAddGuideSheet(onDismiss: () -> Unit) {
    val haptics = rememberGlassHaptics()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = GlassDeepNavy.copy(alpha = 0.96f),
        contentColor = GlassFrostWhite,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "How to Add GlassWidgets",
                    style = MaterialTheme.typography.headlineMedium,
                    color = GlassFrostWhite
                )
                IconButton(
                    onClick = {
                        haptics.click()
                        onDismiss()
                    },
                    modifier = Modifier.testTag("guide_close_button")
                ) {
                    Icon(Icons.Rounded.Close, contentDescription = "Close guide", tint = GlassFrostWhite)
                }
            }

            Text(
                text = "All 840 Liquid Glass presets power just 4 clean home screen slots — zero launcher clutter.",
                style = MaterialTheme.typography.bodyMedium,
                color = GlassFrostWhite.copy(alpha = 0.78f)
            )

            GuideStepCard(
                stepNumber = "1",
                icon = Icons.Rounded.TouchApp,
                title = "Long-Press Your Home Screen",
                detail = "Tap Widgets and scroll down to GlassWidgets."
            )
            GuideStepCard(
                stepNumber = "2",
                icon = Icons.Rounded.GridView,
                title = "Choose 1 of 4 Size Slots",
                detail = "Pick Glass 2×2 (Square), Glass 4×1 (Pill), Glass 4×2 (Medium Card), or Glass 4×4 (Large Dashboard)."
            )
            GuideStepCard(
                stepNumber = "3",
                icon = Icons.Rounded.AutoAwesome,
                title = "Pick Any of 840 Presets",
                detail = "Dropping the widget automatically opens the studio picker across all 28 categories."
            )
            GuideStepCard(
                stepNumber = "4",
                icon = Icons.Rounded.Tune,
                title = "Reconfigure Anytime",
                detail = "Long-press or tap any placed widget to adjust opacity (10%–90%), corner radius (12–32dp), or theme."
            )

            Spacer(Modifier.height(4.dp))
            Button(
                onClick = {
                    haptics.confirm()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = LiquidCyan, contentColor = GlassDeepNavy),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("guide_got_it_button")
            ) {
                Text("Got It — Explore 840 Presets", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}

@Composable
private fun GuideStepCard(
    stepNumber: String,
    icon: ImageVector,
    title: String,
    detail: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .border(1.dp, Color.White.copy(alpha = 0.16f), RoundedCornerShape(18.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(LiquidCyan.copy(alpha = 0.22f))
                .border(1.dp, LiquidCyan.copy(alpha = 0.6f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = LiquidCyan, modifier = Modifier.size(20.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Step $stepNumber · $title",
                color = GlassFrostWhite,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = detail,
                color = GlassFrostWhite.copy(alpha = 0.72f),
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}
