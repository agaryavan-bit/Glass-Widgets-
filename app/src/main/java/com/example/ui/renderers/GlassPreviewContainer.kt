package com.example.ui.renderers

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BorderStyle
import com.example.model.TypographyStyle
import com.example.model.WidgetAlignment
import com.example.model.WidgetDensity
import com.example.model.WidgetInstanceConfig
import com.example.model.WidgetPresetSpec
import com.example.render.GlanceBitmapPainter
import com.example.render.GlassResolvedColors
import com.example.render.GlassVisualTokens
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.PlusJakartaFontFamily
import com.example.ui.theme.SpaceGroteskFontFamily
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun resolveFontFamily(style: TypographyStyle): FontFamily = when (style) {
    TypographyStyle.GEOMETRIC_SANS -> PlusJakartaFontFamily
    TypographyStyle.EDITORIAL_SERIF -> PlayfairDisplayFontFamily
    TypographyStyle.TECHNICAL_MONO -> JetBrainsMonoFontFamily
    TypographyStyle.DISPLAY_BOLD -> SpaceGroteskFontFamily
    TypographyStyle.MINIMAL_LIGHT -> PlusJakartaFontFamily
}

fun resolveFontWeight(style: TypographyStyle): FontWeight = when (style) {
    TypographyStyle.DISPLAY_BOLD -> FontWeight.Bold
    TypographyStyle.MINIMAL_LIGHT -> FontWeight.Light
    TypographyStyle.EDITORIAL_SERIF -> FontWeight.Medium
    TypographyStyle.TECHNICAL_MONO -> FontWeight.SemiBold
    TypographyStyle.GEOMETRIC_SANS -> FontWeight.SemiBold
}

fun resolveHorizontalAlignment(alignment: WidgetAlignment): Alignment.Horizontal = when (alignment) {
    WidgetAlignment.START -> Alignment.Start
    WidgetAlignment.CENTER -> Alignment.CenterHorizontally
    WidgetAlignment.END -> Alignment.End
    WidgetAlignment.SPACE_BETWEEN -> Alignment.Start
}

fun resolveContentPadding(density: WidgetDensity): Dp = when (density) {
    WidgetDensity.AIRY -> 14.dp
    WidgetDensity.BALANCED -> 11.dp
    WidgetDensity.COMPACT -> 8.dp
}

/**
 * Frosted Liquid Glass surface wrapper used by all 28 modular Compose preview renderers.
 * Renders translucent layered glass gradients, optional aurora tint wash, specular top highlight,
 * and crisp 1dp inner highlight rim borders.
 */
@Composable
fun LiquidGlassSurface(
    spec: WidgetPresetSpec,
    config: WidgetInstanceConfig,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.(GlassResolvedColors) -> Unit
) {
    val context = LocalContext.current
    val tokens = remember(spec, config) {
        GlassVisualTokens.resolve(context, spec, config)
    }
    val shape = RoundedCornerShape(tokens.cornerRadiusDp.dp)
    val borderWidth = if (spec.borderStyle == BorderStyle.LUMINOUS_EDGE || spec.borderStyle == BorderStyle.ACCENT_GLOW) 1.3.dp else 1.dp

    Box(
        modifier = modifier
            .clip(shape)
            .background(
                Brush.linearGradient(
                    colors = listOf(tokens.surfaceTop, tokens.surfaceBottom)
                )
            )
            .background(
                Brush.radialGradient(
                    colors = listOf(tokens.tintWash, Color.Transparent),
                    center = Offset(40f, 30f),
                    radius = 360f
                )
            )
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = if (tokens.isLightSurface) 0.32f else 0.14f),
                        Color.Transparent
                    ),
                    endY = 120f
                )
            )
            .border(
                width = borderWidth,
                brush = Brush.linearGradient(
                    colors = listOf(tokens.borderHighlight, tokens.borderSubtle)
                ),
                shape = shape
            )
    ) {
        if (spec.borderStyle == BorderStyle.DOUBLE_GLASS) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(4.dp)
                    .border(
                        width = 0.75.dp,
                        color = tokens.borderSubtle,
                        shape = RoundedCornerShape((tokens.cornerRadiusDp - 4).coerceAtLeast(8).dp)
                    )
            )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(resolveContentPadding(spec.density)),
            content = { content(tokens) }
        )
    }
}

@Composable
fun GlassProgressBar(
    progress: Float,
    tokens: GlassResolvedColors,
    modifier: Modifier = Modifier,
    height: Dp = 6.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(tokens.chipFill)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0.05f, 1f))
                .height(height)
                .clip(CircleShape)
                .background(
                    Brush.horizontalGradient(
                        listOf(tokens.accent, tokens.accent.copy(alpha = 0.7f))
                    )
                )
        )
    }
}

@Composable
fun GlassRingGauge(
    progress: Float,
    label: String,
    tokens: GlassResolvedColors,
    size: Dp = 64.dp,
    strokeWidth: Dp = 6.dp
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size)) {
        Canvas(modifier = Modifier.size(size)) {
            val strokePx = strokeWidth.toPx()
            val inset = strokePx / 2f
            val arcSize = Size(this.size.width - strokePx, this.size.height - strokePx)
            drawArc(
                color = tokens.chipFill,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
            drawArc(
                color = tokens.accent,
                startAngle = -90f,
                sweepAngle = 360f * progress.coerceIn(0.04f, 1f),
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }
        Text(
            text = label,
            color = tokens.textPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = SpaceGroteskFontFamily
        )
    }
}

@Composable
fun GlassAnalogDialCanvas(
    hour: Int,
    minute: Int,
    layoutVariant: Int,
    tokens: GlassResolvedColors,
    size: Dp = 76.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val cx = this.size.width / 2f
        val cy = this.size.height / 2f
        val r = this.size.minDimension * 0.46f

        drawCircle(color = tokens.chipFill, radius = r, center = Offset(cx, cy))
        drawCircle(
            color = if (layoutVariant == 1 || layoutVariant == 5) tokens.accent else tokens.borderHighlight,
            radius = r,
            center = Offset(cx, cy),
            style = Stroke(width = if (layoutVariant == 1) 2.6.dp.toPx() else 1.4.dp.toPx())
        )
        if (layoutVariant == 1 || layoutVariant == 5) {
            drawCircle(
                color = tokens.borderSubtle,
                radius = r * 0.68f,
                center = Offset(cx, cy),
                style = Stroke(width = 1.dp.toPx())
            )
        }

        val tickCount = if (layoutVariant == 2) 24 else 12
        for (i in 0 until tickCount) {
            val angle = (i * (360f / tickCount) - 90f) * PI / 180.0
            val major = if (tickCount == 24) i % 2 == 0 else i % 3 == 0
            val inner = if (major) r * 0.76f else r * 0.85f
            drawLine(
                color = if (i == 0) tokens.accent else if (major) tokens.textPrimary else tokens.textSecondary,
                start = Offset(cx + inner * cos(angle).toFloat(), cy + inner * sin(angle).toFloat()),
                end = Offset(cx + r * 0.92f * cos(angle).toFloat(), cy + r * 0.92f * sin(angle).toFloat()),
                strokeWidth = if (major) 2.dp.toPx() else 1.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        val hourRad = (((hour % 12) + minute / 60f) * 30f - 90f) * PI / 180.0
        drawLine(
            color = tokens.textPrimary,
            start = Offset(cx, cy),
            end = Offset(cx + r * 0.50f * cos(hourRad).toFloat(), cy + r * 0.50f * sin(hourRad).toFloat()),
            strokeWidth = 2.8.dp.toPx(),
            cap = StrokeCap.Round
        )

        val minRad = (minute * 6f - 90f) * PI / 180.0
        drawLine(
            color = tokens.accent,
            start = Offset(cx, cy),
            end = Offset(cx + r * 0.72f * cos(minRad).toFloat(), cy + r * 0.72f * sin(minRad).toFloat()),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )

        drawCircle(color = tokens.textPrimary, radius = 3.dp.toPx(), center = Offset(cx, cy))
    }
}

@Composable
fun GlassCompassCanvas(
    headingDeg: Int,
    tokens: GlassResolvedColors,
    size: Dp = 74.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val cx = this.size.width / 2f
        val cy = this.size.height / 2f
        val r = this.size.minDimension * 0.45f
        drawCircle(color = tokens.chipFill, radius = r, center = Offset(cx, cy))
        drawCircle(color = tokens.borderHighlight, radius = r, center = Offset(cx, cy), style = Stroke(1.4.dp.toPx()))
        for (i in 0 until 16) {
            val rad = (i * 22.5 - 90.0) * PI / 180.0
            val major = i % 4 == 0
            val inner = if (major) r * 0.74f else r * 0.85f
            drawLine(
                color = if (i == 0) tokens.accent else tokens.textSecondary,
                start = Offset(cx + inner * cos(rad).toFloat(), cy + inner * sin(rad).toFloat()),
                end = Offset(cx + r * 0.94f * cos(rad).toFloat(), cy + r * 0.94f * sin(rad).toFloat()),
                strokeWidth = if (major) 2.dp.toPx() else 1.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
        val needleRad = (headingDeg - 90.0) * PI / 180.0
        drawLine(
            color = tokens.accent,
            start = Offset(cx, cy),
            end = Offset(cx + r * 0.60f * cos(needleRad).toFloat(), cy + r * 0.60f * sin(needleRad).toFloat()),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = tokens.textSecondary,
            start = Offset(cx, cy),
            end = Offset(cx - r * 0.40f * cos(needleRad).toFloat(), cy - r * 0.40f * sin(needleRad).toFloat()),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawCircle(color = tokens.textPrimary, radius = 3.dp.toPx(), center = Offset(cx, cy))
    }
}

@Composable
fun GlassQrCodeCanvas(
    payload: String,
    tokens: GlassResolvedColors,
    size: Dp = 74.dp
) {
    val matrix = remember(payload) {
        GlanceBitmapPainter.buildQrMatrix(payload.ifBlank { "GlassWidgets" }, gridSize = 19)
    }
    Canvas(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(tokens.chipFill)
            .padding(6.dp)
    ) {
        val n = matrix.size
        val cell = this.size.minDimension / n.toFloat()
        for (r in 0 until n) {
            for (c in 0 until n) {
                if (matrix[r][c]) {
                    val isFinder = (r < 7 && c < 7) || (r < 7 && c >= n - 7) || (r >= n - 7 && c < 7)
                    drawRoundRect(
                        color = if (isFinder) tokens.accent else tokens.textPrimary,
                        topLeft = Offset(c * cell + 0.5f, r * cell + 0.5f),
                        size = Size((cell - 1f).coerceAtLeast(1f), (cell - 1f).coerceAtLeast(1f)),
                        cornerRadius = CornerRadius(cell * 0.25f, cell * 0.25f)
                    )
                }
            }
        }
    }
}

@Composable
fun GlassBadgeChip(
    text: String,
    tokens: GlassResolvedColors,
    accented: Boolean = false
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (accented) tokens.accent.copy(alpha = 0.24f) else tokens.chipFill)
            .border(
                width = 0.8.dp,
                color = if (accented) tokens.accent.copy(alpha = 0.6f) else tokens.borderSubtle,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            color = if (accented) tokens.accent else tokens.textPrimary,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = PlusJakartaFontFamily,
            maxLines = 1
        )
    }
}
