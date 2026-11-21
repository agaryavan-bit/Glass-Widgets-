package com.example.render

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.net.Uri
import com.example.R
import com.example.model.BorderStyle
import com.example.model.WidgetSlotSize
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * Memory-bounded Android Canvas Bitmap renderer for Jetpack Glance widgets.
 * Produces crisp Liquid Glass frosted panels (adjustable opacity 10%-90%, corner radius 12-32dp,
 * 1dp specular highlight borders), analog clock dials, progress rings, compass dials, QR codes,
 * and framed local/sample photos without exceeding RemoteViews bitmap memory limits.
 */
object GlanceBitmapPainter {

    fun drawLiquidGlassBackground(
        slotSize: WidgetSlotSize,
        tokens: GlassResolvedColors,
        borderStyle: BorderStyle
    ): Bitmap {
        val (width, height) = when (slotSize) {
            WidgetSlotSize.SIZE_2X2 -> 280 to 280
            WidgetSlotSize.SIZE_4X1 -> 480 to 132
            WidgetSlotSize.SIZE_4X2 -> 480 to 250
            WidgetSlotSize.SIZE_4X4 -> 440 to 440
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val radiusPx = (tokens.cornerRadiusDp * 1.75f).coerceIn(16f, min(width, height) / 2.2f)
        val rect = RectF(2f, 2f, width - 2f, height - 2f)

        // 1. Base translucent frosted gradient layer
        val basePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, width.toFloat(), height.toFloat(),
                tokens.surfaceTopArgb,
                tokens.surfaceBottomArgb,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(rect, radiusPx, radiusPx, basePaint)

        // 2. Subtle accent/aurora glass refraction sheen
        if ((tokens.tintWashArgb ushr 24) > 0) {
            val washPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                shader = RadialGradient(
                    width * 0.22f,
                    height * 0.18f,
                    width * 0.85f,
                    tokens.tintWashArgb,
                    0x00000000,
                    Shader.TileMode.CLAMP
                )
            }
            canvas.drawRoundRect(rect, radiusPx, radiusPx, washPaint)
        }

        // 3. Top-edge liquid specular reflection band
        val specPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, 0f, height * 0.42f,
                GlassVisualTokens.withAlpha(0xFFFFFF, if (tokens.isLightSurface) 95 else 42),
                0x00FFFFFF,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(RectF(4f, 4f, width - 4f, height - 4f), radiusPx - 2f, radiusPx - 2f, specPaint)

        // 4. Crisp 1dp inner specular highlight rim border
        val strokeWidth = if (borderStyle == BorderStyle.LUMINOUS_EDGE || borderStyle == BorderStyle.ACCENT_GLOW) 2.5f else 2f
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            this.strokeWidth = strokeWidth
            shader = LinearGradient(
                0f, 0f, width.toFloat(), height.toFloat(),
                tokens.borderHighlightArgb,
                tokens.borderSubtleArgb,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(RectF(2.5f, 2.5f, width - 2.5f, height - 2.5f), radiusPx, radiusPx, borderPaint)

        if (borderStyle == BorderStyle.DOUBLE_GLASS) {
            val innerBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                this.strokeWidth = 1.4f
                color = tokens.borderSubtleArgb
            }
            canvas.drawRoundRect(
                RectF(9f, 9f, width - 9f, height - 9f),
                (radiusPx - 6f).coerceAtLeast(8f),
                (radiusPx - 6f).coerceAtLeast(8f),
                innerBorder
            )
        }

        return bitmap
    }

    fun drawAnalogClockDial(
        sizePx: Int,
        hour: Int,
        minute: Int,
        layoutVariant: Int,
        tokens: GlassResolvedColors
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val cx = sizePx / 2f
        val cy = sizePx / 2f
        val r = sizePx * 0.44f

        // Frosted inner glass bezel
        val ringBg = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = tokens.chipFillArgb
        }
        canvas.drawCircle(cx, cy, r, ringBg)

        val ringStroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = if (layoutVariant == 1) 4.5f else 2.2f
            color = if (layoutVariant == 1 || layoutVariant == 5) tokens.accentArgb else tokens.borderHighlightArgb
        }
        canvas.drawCircle(cx, cy, r, ringStroke)

        if (layoutVariant == 1 || layoutVariant == 5) {
            val innerRing = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeWidth = 1.6f
                color = tokens.borderSubtleArgb
            }
            canvas.drawCircle(cx, cy, r * 0.72f, innerRing)
        }

        // Markers: Roman / 60-tick / 12-dot / Minimal
        val tickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = tokens.textSecondaryArgb
            strokeCap = Paint.Cap.ROUND
        }
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = tokens.textPrimaryArgb
            textAlign = Paint.Align.CENTER
            textSize = sizePx * 0.095f
            isFakeBoldText = true
        }

        when (layoutVariant % 5) {
            3 -> {
                // Roman heritage markers
                val romans = listOf("XII", "III", "VI", "IX")
                val angles = listOf(-90.0, 0.0, 90.0, 180.0)
                romans.forEachIndexed { idx, label ->
                    val rad = Math.toRadians(angles[idx])
                    val tx = cx + (r * 0.76f) * cos(rad).toFloat()
                    val ty = cy + (r * 0.76f) * sin(rad).toFloat() + textPaint.textSize * 0.35f
                    canvas.drawText(label, tx, ty, textPaint)
                }
            }
            2 -> {
                // 60 precision ticks
                for (i in 0 until 60) {
                    val rad = (i * 6.0 - 90.0) * PI / 180.0
                    val major = i % 5 == 0
                    val inner = if (major) r * 0.80f else r * 0.88f
                    tickPaint.strokeWidth = if (major) 2.8f else 1.2f
                    tickPaint.color = if (major) tokens.textPrimaryArgb else tokens.textSecondaryArgb
                    canvas.drawLine(
                        cx + inner * cos(rad).toFloat(),
                        cy + inner * sin(rad).toFloat(),
                        cx + r * 0.94f * cos(rad).toFloat(),
                        cy + r * 0.94f * sin(rad).toFloat(),
                        tickPaint
                    )
                }
            }
            else -> {
                // 12 sleek hour indices
                for (i in 0 until 12) {
                    val rad = (i * 30.0 - 90.0) * PI / 180.0
                    val isCardinal = i % 3 == 0
                    val inner = if (isCardinal) r * 0.78f else r * 0.85f
                    tickPaint.strokeWidth = if (isCardinal) 3.2f else 1.8f
                    tickPaint.color = if (i == 0) tokens.accentArgb else tokens.textPrimaryArgb
                    canvas.drawLine(
                        cx + inner * cos(rad).toFloat(),
                        cy + inner * sin(rad).toFloat(),
                        cx + r * 0.93f * cos(rad).toFloat(),
                        cy + r * 0.93f * sin(rad).toFloat(),
                        tickPaint
                    )
                }
            }
        }

        // Hour hand
        val hourAngle = (((hour % 12) + minute / 60f) * 30f - 90f) * PI / 180.0
        val hourPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = tokens.textPrimaryArgb
            strokeWidth = sizePx * 0.036f
            strokeCap = Paint.Cap.ROUND
        }
        canvas.drawLine(
            cx, cy,
            cx + r * 0.50f * cos(hourAngle).toFloat(),
            cy + r * 0.50f * sin(hourAngle).toFloat(),
            hourPaint
        )

        // Minute hand
        val minAngle = (minute * 6f - 90f) * PI / 180.0
        val minPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = tokens.accentArgb
            strokeWidth = sizePx * 0.025f
            strokeCap = Paint.Cap.ROUND
        }
        canvas.drawLine(
            cx, cy,
            cx + r * 0.72f * cos(minAngle).toFloat(),
            cy + r * 0.72f * sin(minAngle).toFloat(),
            minPaint
        )

        // Center hub dot
        val hubPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = tokens.textPrimaryArgb }
        canvas.drawCircle(cx, cy, sizePx * 0.035f, hubPaint)

        return bitmap
    }

    fun drawProgressRing(
        sizePx: Int,
        progress: Float,
        centerLabel: String,
        tokens: GlassResolvedColors
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val stroke = sizePx * 0.10f
        val rect = RectF(stroke, stroke, sizePx - stroke, sizePx - stroke)

        val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = stroke
            color = tokens.chipFillArgb
        }
        canvas.drawArc(rect, 0f, 360f, false, trackPaint)

        val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = stroke
            strokeCap = Paint.Cap.ROUND
            color = tokens.accentArgb
        }
        canvas.drawArc(rect, -90f, 360f * progress.coerceIn(0.03f, 1f), false, arcPaint)

        if (centerLabel.isNotEmpty()) {
            val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = tokens.textPrimaryArgb
                textAlign = Paint.Align.CENTER
                textSize = sizePx * 0.22f
                isFakeBoldText = true
            }
            val fm = textPaint.fontMetrics
            val baseline = sizePx / 2f - (fm.ascent + fm.descent) / 2f
            canvas.drawText(centerLabel, sizePx / 2f, baseline, textPaint)
        }
        return bitmap
    }

    fun drawCompassDial(
        sizePx: Int,
        headingDeg: Int,
        tokens: GlassResolvedColors
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val cx = sizePx / 2f
        val cy = sizePx / 2f
        val r = sizePx * 0.43f

        val ring = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2.5f
            color = tokens.borderHighlightArgb
        }
        canvas.drawCircle(cx, cy, r, ring)

        val labels = listOf("N" to -90.0, "E" to 0.0, "S" to 90.0, "W" to 180.0)
        val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textAlign = Paint.Align.CENTER
            textSize = sizePx * 0.12f
            isFakeBoldText = true
        }
        labels.forEach { (dir, deg) ->
            val rad = Math.toRadians(deg)
            labelPaint.color = if (dir == "N") tokens.accentArgb else tokens.textSecondaryArgb
            val x = cx + r * 0.74f * cos(rad).toFloat()
            val y = cy + r * 0.74f * sin(rad).toFloat() + labelPaint.textSize * 0.35f
            canvas.drawText(dir, x, y, labelPaint)
        }

        // Needle pointing toward heading
        val needleRad = Math.toRadians(headingDeg - 90.0)
        val northPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = tokens.accentArgb
            strokeWidth = sizePx * 0.045f
            strokeCap = Paint.Cap.ROUND
        }
        canvas.drawLine(
            cx, cy,
            cx + r * 0.56f * cos(needleRad).toFloat(),
            cy + r * 0.56f * sin(needleRad).toFloat(),
            northPaint
        )
        val southPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = tokens.textSecondaryArgb
            strokeWidth = sizePx * 0.03f
            strokeCap = Paint.Cap.ROUND
        }
        canvas.drawLine(
            cx, cy,
            cx - r * 0.42f * cos(needleRad).toFloat(),
            cy - r * 0.42f * sin(needleRad).toFloat(),
            southPaint
        )
        canvas.drawCircle(cx, cy, sizePx * 0.04f, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = tokens.textPrimaryArgb })
        return bitmap
    }

    /**
     * Generates a clean, crisp deterministic QR-style glass matrix bitmap from any custom text.
     */
    fun drawDeterministicQrCode(
        sizePx: Int,
        payload: String,
        tokens: GlassResolvedColors
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val matrix = buildQrMatrix(payload.ifBlank { "GlassWidgets" })
        val gridSize = matrix.size
        val pad = sizePx * 0.08f
        val cell = (sizePx - pad * 2f) / gridSize.toFloat()

        val bg = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = tokens.chipFillArgb }
        canvas.drawRoundRect(RectF(0f, 0f, sizePx.toFloat(), sizePx.toFloat()), 18f, 18f, bg)

        val modulePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = tokens.textPrimaryArgb }
        val finderAccent = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = tokens.accentArgb }

        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                if (matrix[r][c]) {
                    val left = pad + c * cell + 0.6f
                    val top = pad + r * cell + 0.6f
                    val isFinder = (r < 7 && c < 7) || (r < 7 && c >= gridSize - 7) || (r >= gridSize - 7 && c < 7)
                    canvas.drawRoundRect(
                        RectF(left, top, left + cell - 1.2f, top + cell - 1.2f),
                        cell * 0.25f,
                        cell * 0.25f,
                        if (isFinder) finderAccent else modulePaint
                    )
                }
            }
        }
        return bitmap
    }

    fun buildQrMatrix(text: String, gridSize: Int = 21): Array<BooleanArray> {
        val matrix = Array(gridSize) { BooleanArray(gridSize) }

        fun placeFinder(startR: Int, startC: Int) {
            for (r in 0 until 7) {
                for (c in 0 until 7) {
                    val border = r == 0 || r == 6 || c == 0 || c == 6
                    val center = r in 2..4 && c in 2..4
                    matrix[startR + r][startC + c] = border || center
                }
            }
        }
        placeFinder(0, 0)
        placeFinder(0, gridSize - 7)
        placeFinder(gridSize - 7, 0)

        var seed = 2166136261L
        text.forEach { ch ->
            seed = (seed xor ch.code.toLong()) * 16777619L
        }
        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                val inFinder = (r < 8 && c < 8) || (r < 8 && c >= gridSize - 8) || (r >= gridSize - 8 && c < 8)
                if (!inFinder) {
                    seed = (seed * 6364136223846793005L + 1442695040888963407L)
                    matrix[r][c] = ((seed ushr 33) and 1L) == 1L
                }
            }
        }
        return matrix
    }

    fun loadRoundedPhotoBitmap(
        context: Context,
        photoUri: String,
        fallbackResId: Int = R.drawable.img_sample_photo_1795251487510,
        targetW: Int = 300,
        targetH: Int = 220,
        cornerRadiusPx: Float = 28f
    ): Bitmap {
        val decoded: Bitmap? = if (photoUri.isNotBlank()) {
            runCatching {
                val uri = Uri.parse(photoUri)
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val opts = BitmapFactory.Options().apply { inSampleSize = 4 }
                    BitmapFactory.decodeStream(stream, null, opts)
                }
            }.getOrNull()
        } else {
            null
        } ?: runCatching {
            val opts = BitmapFactory.Options().apply { inSampleSize = 2 }
            BitmapFactory.decodeResource(context.resources, fallbackResId, opts)
        }.getOrNull()

        val output = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        if (decoded != null) {
            val scaled = Bitmap.createScaledBitmap(decoded, targetW, targetH, true)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                shader = BitmapShader(scaled, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            }
            canvas.drawRoundRect(
                RectF(0f, 0f, targetW.toFloat(), targetH.toFloat()),
                cornerRadiusPx,
                cornerRadiusPx,
                paint
            )
            if (scaled != decoded) scaled.recycle()
        } else {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF1E293B.toInt() }
            canvas.drawRoundRect(
                RectF(0f, 0f, targetW.toFloat(), targetH.toFloat()),
                cornerRadiusPx,
                cornerRadiusPx,
                paint
            )
        }
        return output
    }
}
