package com.bucharest.qurio.presentation.game.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.PathParser
import com.bucharest.qurio.R
import com.bucharest.qurio.presentation.constants.PresentationConstants

class GameTimerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val orangeColor = ContextCompat.getColor(context, R.color.orange)
    private val fillColor = ContextCompat.getColor(context, R.color.surface_high)
    private val strokeColor = ContextCompat.getColor(context, R.color.shade_quad)

    private val orangePaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL; color = orangeColor }
    private val fillPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL; color = fillColor }
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE; color = strokeColor; strokeWidth = 1f
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        textSize = 36f
        typeface = ResourcesCompat.getFont(context, R.font.fredoka_one_regular)
    }

    private var progress = 0.6f
    private var centerText = "50%"
    private val capDxPerDy = PresentationConstants.CAP_DX_PER_DY

    private val sideSmallObjectsPath =
        "M 13.624 20.222 L 16.871 20.222 L 28.238 28 L 24.99 28 L 13.624 20.222 Z M 338.376 20.222 L 335.129 20.222 L 323.762 28 L 327.01 28 L 338.376 20.222 Z"

    private val sideBigObjectsPath =
        "M 19.758 8 L 15.247 13.556 L 36.356 28 L 33.109 28 L 12 13.556 L 16.511 8 L 19.758 8 Z M 332.242 8 L 336.753 13.556 L 315.644 28 L 318.891 28 L 340 13.556 L 335.489 8 L 332.242 8 Z"

    private val containerFillPath =
        "M 335.245 8.5 L 339.269 13.449 L 318.735 27.5 L 33.265 27.5 L 12.729 13.448 L 16.749 8.5 L 335.245 8.5 Z"

    private val containerStrokePath =
        "M 335.245 8.5 L 339.269 13.449 L 318.735 27.5 L 33.265 27.5 L 12.729 13.448 L 16.749 8.5 L 335.245 8.5 Z"

    private val progressShapePath =
        "M 18 13 L 21.058 10 L 330.867 10 L 334 12.667 L 315.652 26 L 36.497 26 L 18 13 Z"

    private val pathSideSmall = Path()
    private val pathSideBig = Path()
    private val pathContainerFill = Path()
    private val pathContainerStroke = Path()
    private val pathProgressFull = Path()
    private val bounds = RectF()

    init {
        context.obtainStyledAttributes(attrs, R.styleable.GameTimerView).apply {
            progress = getFloat(R.styleable.GameTimerView_progress, progress)
            centerText = getString(R.styleable.GameTimerView_centerText) ?: centerText
            recycle()
        }

        pathSideSmall.set(PathParser.createPathFromPathData(sideSmallObjectsPath))
        pathSideBig.set(PathParser.createPathFromPathData(sideBigObjectsPath))
        pathContainerFill.set(PathParser.createPathFromPathData(containerFillPath))
        pathContainerStroke.set(PathParser.createPathFromPathData(containerStrokePath))
        pathProgressFull.set(PathParser.createPathFromPathData(progressShapePath))

        pathContainerFill.computeBounds(bounds, true)
    }

    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, 1f)
        invalidate()
    }

    fun setCenterText(value: String) {
        centerText = value
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()

        val matrix = createScaleMatrix(w, h)
        val scaledPaths = createScaledPaths(matrix)

        drawContainer(canvas, scaledPaths.fill, scaledPaths.stroke)
        drawProgressBar(canvas, scaledPaths.progress, w, h)
        drawSideDecorations(canvas, scaledPaths.big, scaledPaths.small)
        drawCenteredText(canvas, w, h)
    }

    private fun createScaleMatrix(w: Float, h: Float): Matrix {
        val scaleX = w / bounds.width()
        val scaleY = h / bounds.height()
        return Matrix().apply {
            setScale(scaleX, scaleY)
            postTranslate(-bounds.left * scaleX, -bounds.top * scaleY)
        }
    }

    private data class ScaledPaths(
        val fill: Path,
        val stroke: Path,
        val progress: Path,
        val big: Path,
        val small: Path
    )

    private fun createScaledPaths(matrix: Matrix): ScaledPaths {
        return ScaledPaths(
            Path(pathContainerFill).apply { transform(matrix) },
            Path(pathContainerStroke).apply { transform(matrix) },
            Path(pathProgressFull).apply { transform(matrix) },
            Path(pathSideBig).apply { transform(matrix) },
            Path(pathSideSmall).apply { transform(matrix) },
        )
    }

    private fun drawContainer(canvas: Canvas, fill: Path, stroke: Path) {
        canvas.drawPath(fill, fillPaint)
        canvas.drawPath(stroke, strokePaint)
    }

    private fun drawProgressBar(canvas: Canvas, scaledProgress: Path, w: Float, h: Float) {
        val progressW = (w * progress).coerceIn(0f, w)
        val capWidth = capDxPerDy * h
        val xCapBase = progressW
        val xCapBottom = (progressW - capWidth).coerceAtLeast(0f)

        val clipProgress = Path().apply {
            moveTo(0f, 0f)
            lineTo(xCapBase, 0f)
            lineTo(xCapBottom, h)
            lineTo(0f, h)
            close()
        }

        val finalProgress = Path().apply {
            op(scaledProgress, clipProgress, Path.Op.INTERSECT)
        }

        canvas.drawPath(finalProgress, orangePaint)
    }

    private fun drawSideDecorations(canvas: Canvas, big: Path, small: Path) {
        canvas.drawPath(big, orangePaint)
        canvas.drawPath(small, orangePaint)
    }

    private fun drawCenteredText(canvas: Canvas, w: Float, h: Float) {
        textPaint.textSize = h * PresentationConstants.TIMER_TEXT_SIZE_MULTIPLIER
        val textY = h / 2f - (textPaint.descent() + textPaint.ascent()) / 2f
        canvas.drawText(centerText, w / 2f, textY, textPaint)
    }
}


