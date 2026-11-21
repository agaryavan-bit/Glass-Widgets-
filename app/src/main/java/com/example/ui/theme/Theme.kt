package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LiquidDarkColorScheme = darkColorScheme(
    primary = LiquidCyan,
    onPrimary = GlassMidnight,
    primaryContainer = Color(0xFF0E3A5C),
    onPrimaryContainer = Color(0xFFD6F2FF),
    secondary = AuroraViolet,
    onSecondary = GlassMidnight,
    secondaryContainer = Color(0xFF2B224B),
    onSecondaryContainer = Color(0xFFEDE9FE),
    tertiary = SunsetCoral,
    onTertiary = GlassMidnight,
    background = GlassMidnight,
    onBackground = GlassFrostWhite,
    surface = GlassDeepNavy,
    onSurface = GlassFrostWhite,
    surfaceVariant = GlassObsidian,
    onSurfaceVariant = Color(0xFFB4C5E0),
    outline = Color(0x55FFFFFF),
    outlineVariant = Color(0x28FFFFFF)
)

private val LiquidLightColorScheme = lightColorScheme(
    primary = Color(0xFF0284C7),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF082F49),
    secondary = Color(0xFF7C3AED),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEDE9FE),
    onSecondaryContainer = Color(0xFF2E1065),
    tertiary = Color(0xFFE11D48),
    onTertiary = Color.White,
    background = GlassFrostWhite,
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = GlassMist,
    onSurfaceVariant = Color(0xFF334155),
    outline = Color(0xFF94A3B8),
    outlineVariant = Color(0xFFCBD5E1)
)

@Composable
fun GlassWidgetsTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> LiquidDarkColorScheme
        else -> LiquidLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) = GlassWidgetsTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
