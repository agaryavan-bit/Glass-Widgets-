package com.example.render

import android.content.Context
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.example.model.AccentMode
import com.example.model.BorderStyle
import com.example.model.GlassFinish
import com.example.model.GlassTheme
import com.example.model.WidgetInstanceConfig
import com.example.model.WidgetPresetSpec
import kotlin.math.roundToInt

data class GlassResolvedColors(
    val surfaceTopArgb: Int,
    val surfaceBottomArgb: Int,
    val tintWashArgb: Int,
    val borderHighlightArgb: Int,
    val borderSubtleArgb: Int,
    val textPrimaryArgb: Int,
    val textSecondaryArgb: Int,
    val accentArgb: Int,
    val chipFillArgb: Int,
    val isLightSurface: Boolean,
    val cornerRadiusDp: Int,
    val opacity: Float
) {
    val surfaceTop: Color get() = Color(surfaceTopArgb)
    val surfaceBottom: Color get() = Color(surfaceBottomArgb)
    val tintWash: Color get() = Color(tintWashArgb)
    val borderHighlight: Color get() = Color(borderHighlightArgb)
    val borderSubtle: Color get() = Color(borderSubtleArgb)
    val textPrimary: Color get() = Color(textPrimaryArgb)
    val textSecondary: Color get() = Color(textSecondaryArgb)
    val accent: Color get() = Color(accentArgb)
    val chipFill: Color get() = Color(chipFillArgb)
}

object GlassVisualTokens {

    fun resolve(
        context: Context?,
        spec: WidgetPresetSpec,
        config: WidgetInstanceConfig
    ): GlassResolvedColors {
        val opacity = config.opacity.coerceIn(0.10f, 0.90f)
        val radius = config.cornerRadiusDp.coerceIn(12, 32)
        val theme = config.glassTheme

        val dynamicAccent = if (theme == GlassTheme.MATERIAL_YOU && context != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            runCatching {
                ContextCompat.getColor(context, android.R.color.system_accent1_400)
            }.getOrElse { config.accentColorArgb.toInt() }
        } else {
            config.accentColorArgb.toInt()
        }

        val dynamicSurfaceBase = if (theme == GlassTheme.MATERIAL_YOU && context != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            runCatching {
                ContextCompat.getColor(context, android.R.color.system_neutral1_900)
            }.getOrElse { 0xFF152038.toInt() }
        } else {
            0xFF152038.toInt()
        }

        val isLight = theme == GlassTheme.LIGHT_FROSTED

        val baseTopRgb = when (theme) {
            GlassTheme.LIGHT_FROSTED -> when (spec.glassFinish) {
                GlassFinish.FROSTED_MILK -> 0xFFFFFF
                GlassFinish.PRISM_TINT -> 0xEEF6FF
                GlassFinish.AURORA_GLOW -> 0xF5F0FF
                else -> 0xF7FAFF
            }
            GlassTheme.DARK_OBSIDIAN -> when (spec.glassFinish) {
                GlassFinish.OBSIDIAN_SMOKE -> 0x0D1424
                GlassFinish.PRISM_TINT -> 0x111E36
                GlassFinish.AURORA_GLOW -> 0x1A1536
                else -> 0x131C30
            }
            GlassTheme.MATERIAL_YOU -> dynamicSurfaceBase and 0x00FFFFFF
            GlassTheme.CUSTOM -> 0x121A2E
        }

        val baseBottomRgb = when (theme) {
            GlassTheme.LIGHT_FROSTED -> 0xDCE8FA
            GlassTheme.DARK_OBSIDIAN -> 0x070B14
            GlassTheme.MATERIAL_YOU -> blendRgb(dynamicSurfaceBase and 0x00FFFFFF, dynamicAccent and 0x00FFFFFF, 0.22f)
            GlassTheme.CUSTOM -> blendRgb(0x0B1220, dynamicAccent and 0x00FFFFFF, 0.28f)
        }

        val topAlpha = (opacity * 255f).roundToInt().coerceIn(25, 232)
        val bottomAlpha = ((opacity * 0.84f) * 255f).roundToInt().coerceIn(20, 224)

        val surfaceTopArgb = withAlpha(baseTopRgb, topAlpha)
        val surfaceBottomArgb = withAlpha(baseBottomRgb, bottomAlpha)

        val washStrength = when (spec.accentMode) {
            AccentMode.GRADIENT_WASH -> 0.26f
            AccentMode.DUOTONE -> 0.20f
            AccentMode.VIVID_BADGE -> 0.12f
            AccentMode.SUBTLE_TINT -> 0.08f
            AccentMode.MONOCHROME -> 0.0f
        }
        val tintWashArgb = withAlpha(dynamicAccent and 0x00FFFFFF, (washStrength * 255f).roundToInt().coerceIn(0, 90))

        val borderHighlightArgb = when (spec.borderStyle) {
            BorderStyle.LUMINOUS_EDGE -> withAlpha(0xFFFFFF, if (isLight) 230 else 155)
            BorderStyle.ACCENT_GLOW -> withAlpha(dynamicAccent and 0x00FFFFFF, 195)
            BorderStyle.FROSTED_RIM -> withAlpha(0xFFFFFF, if (isLight) 200 else 120)
            BorderStyle.DOUBLE_GLASS -> withAlpha(0xFFFFFF, if (isLight) 215 else 140)
            BorderStyle.HAIRLINE -> withAlpha(0xFFFFFF, if (isLight) 165 else 90)
        }

        val borderSubtleArgb = when (spec.borderStyle) {
            BorderStyle.ACCENT_GLOW -> withAlpha(dynamicAccent and 0x00FFFFFF, 85)
            else -> withAlpha(if (isLight) 0x94A3B8 else 0xFFFFFF, if (isLight) 95 else 38)
        }

        val primaryText = when (theme) {
            GlassTheme.CUSTOM -> config.textColorArgb.toInt()
            GlassTheme.LIGHT_FROSTED -> 0xFF0F172A.toInt()
            GlassTheme.DARK_OBSIDIAN, GlassTheme.MATERIAL_YOU -> 0xFFF8FAFC.toInt()
        }

        val secondaryText = withAlpha(primaryText and 0x00FFFFFF, if (isLight) 175 else 185)

        val resolvedAccent = if (spec.accentMode == AccentMode.MONOCHROME) {
            withAlpha(primaryText and 0x00FFFFFF, 220)
        } else {
            dynamicAccent
        }

        val chipFill = if (isLight) {
            withAlpha(0x0F172A, 22)
        } else {
            withAlpha(0xFFFFFF, 28)
        }

        return GlassResolvedColors(
            surfaceTopArgb = surfaceTopArgb,
            surfaceBottomArgb = surfaceBottomArgb,
            tintWashArgb = tintWashArgb,
            borderHighlightArgb = borderHighlightArgb,
            borderSubtleArgb = borderSubtleArgb,
            textPrimaryArgb = primaryText,
            textSecondaryArgb = secondaryText,
            accentArgb = resolvedAccent,
            chipFillArgb = chipFill,
            isLightSurface = isLight,
            cornerRadiusDp = radius,
            opacity = opacity
        )
    }

    fun withAlpha(rgb: Int, alpha: Int): Int {
        return ((alpha.coerceIn(0, 255) shl 24) or (rgb and 0x00FFFFFF))
    }

    private fun blendRgb(rgbA: Int, rgbB: Int, ratio: Float): Int {
        val rA = (rgbA shr 16) and 0xFF
        val gA = (rgbA shr 8) and 0xFF
        val bA = rgbA and 0xFF
        val rB = (rgbB shr 16) and 0xFF
        val gB = (rgbB shr 8) and 0xFF
        val bB = rgbB and 0xFF
        val r = (rA + (rB - rA) * ratio).roundToInt().coerceIn(0, 255)
        val g = (gA + (gB - gA) * ratio).roundToInt().coerceIn(0, 255)
        val b = (bA + (bB - bA) * ratio).roundToInt().coerceIn(0, 255)
        return (r shl 16) or (g shl 8) or b
    }
}
