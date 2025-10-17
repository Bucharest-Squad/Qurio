package com.bucharest.qurio.presentation.game.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.bucharest.qurio.R

class OutlineTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val outlinePaint = Paint()
    private val shadowPaint = Paint()

    private var outlineColor: Int = Color.TRANSPARENT
    private var outlineWidth: Float = 0f

    private var gradientStartColor: Int = Color.TRANSPARENT
    private var gradientEndColor: Int = Color.TRANSPARENT
    private var gradientAngle: Int = 90
    private var useGradient: Boolean = false

    private var shadowColor: Int = Color.TRANSPARENT
    private var shadowRadius: Float = 0f
    private var shadowDx: Float = 0f
    private var shadowDy: Float = 0f
    private var useShadow: Boolean = false

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.OutlineTextView,
            0, 0
        ).apply {
            try {
                outlineColor = getColor(R.styleable.OutlineTextView_outlineColor, Color.TRANSPARENT)
                outlineWidth = getDimension(R.styleable.OutlineTextView_outlineWidth, 0f)

                gradientStartColor =
                    getColor(R.styleable.OutlineTextView_gradientStartColor, Color.TRANSPARENT)
                gradientEndColor =
                    getColor(R.styleable.OutlineTextView_gradientEndColor, Color.TRANSPARENT)
                gradientAngle = getInt(R.styleable.OutlineTextView_gradientAngle, 90)
                useGradient =
                    gradientStartColor != Color.TRANSPARENT && gradientEndColor != Color.TRANSPARENT

                shadowColor = getColor(R.styleable.OutlineTextView_shadowColor, Color.TRANSPARENT)
                shadowRadius = getDimension(R.styleable.OutlineTextView_shadowRadius, 0f)
                shadowDx = getDimension(R.styleable.OutlineTextView_shadowDx, 0f)
                shadowDy = getDimension(R.styleable.OutlineTextView_shadowDy, 0f)
                useShadow = shadowColor != Color.TRANSPARENT && shadowRadius > 0
            } finally {
                recycle()
            }
        }

        outlinePaint.isAntiAlias = true
        outlinePaint.style = Paint.Style.STROKE
        outlinePaint.strokeWidth = outlineWidth
        outlinePaint.color = outlineColor

        if (useShadow) {
            shadowPaint.isAntiAlias = true
            shadowPaint.style = Paint.Style.FILL
            shadowPaint.color = currentTextColor
            shadowPaint.setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor)
        }

        val paddingIncrease =
            (outlineWidth + shadowRadius + Math.max(Math.abs(shadowDx), Math.abs(shadowDy))).toInt()
        setPadding(
            paddingLeft + paddingIncrease,
            paddingTop + paddingIncrease,
            paddingRight + paddingIncrease,
            paddingBottom + paddingIncrease
        )

        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val textStr = text?.toString() ?: return

        val baseX = paddingLeft.toFloat()
        val baseY = baseline.toFloat()

        outlinePaint.textSize = textSize
        outlinePaint.typeface = typeface
        shadowPaint.textSize = textSize
        shadowPaint.typeface = typeface

        if (useGradient) {
            val gradient = when (gradientAngle) {
                0 -> LinearGradient(
                    0f, 0f, width.toFloat(), 0f,
                    gradientStartColor, gradientEndColor,
                    Shader.TileMode.CLAMP
                )

                90 -> LinearGradient(
                    0f, 0f, 0f, height.toFloat(),
                    gradientStartColor, gradientEndColor,
                    Shader.TileMode.CLAMP
                )

                45 -> LinearGradient(
                    0f, 0f, width.toFloat(), height.toFloat(),
                    gradientStartColor, gradientEndColor,
                    Shader.TileMode.CLAMP
                )

                else -> LinearGradient(
                    0f, 0f, 0f, height.toFloat(),
                    gradientStartColor, gradientEndColor,
                    Shader.TileMode.CLAMP
                )
            }
            paint.shader = gradient
        }

        if (useShadow) {
            canvas.drawText(textStr, baseX, baseY, shadowPaint)
        }

        canvas.drawText(textStr, baseX, baseY, outlinePaint)

        super.onDraw(canvas)

        paint.shader = null
    }

    fun setOutlineColor(color: Int) {
        outlineColor = color
        outlinePaint.color = color
        invalidate()
    }

    fun setTextColorDynamic(color: Int) {
        setTextColor(color)
        invalidate()
    }

    fun setGradientColors(startColor: Int, endColor: Int, angle: Int = 90) {
        gradientStartColor = startColor
        gradientEndColor = endColor
        gradientAngle = angle
        useGradient = true
        invalidate()
    }

    fun clearGradient() {
        useGradient = false
        paint.shader = null
        invalidate()
    }
}
