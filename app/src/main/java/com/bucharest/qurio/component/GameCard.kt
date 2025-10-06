package com.bucharest.qurio.component

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.bucharest.qurio.databinding.ItemGameCardBinding

class GameCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ItemGameCardBinding.inflate(LayoutInflater.from(context), this, true)

    fun setState(
        title: String,
        @DrawableRes imageRes: Int,
        startColor: Int,
        endColor: Int
    ) {
        setTitle(title)
        setImage(imageRes)
        setGradientColors(startColor,endColor)
    }

    private fun setTitle(title: String) {
        binding.categoryTitle.text = title
    }

    private fun setImage(@DrawableRes imageRes: Int) {
        binding.categoryThumbnail.setImageResource(imageRes)
    }

    private fun setGradientColors(@ColorInt startColor: Int, @ColorInt endColor: Int) {
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(startColor, endColor)
        ).apply {
            cornerRadii = floatArrayOf(
                0f, 0f, 0f, 0f,
                28f.dp, 28f.dp, 28f.dp, 28f.dp
            )
        }
        binding.gradientOverlay.background = gradient
    }

    private val Float.dp: Float get() = this * resources.displayMetrics.density}