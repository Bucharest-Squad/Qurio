package com.bucharest.qurio.presentation.home.components

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class CategoryCarouselTransformer : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        page.apply {
            pivotX = width * PIVOT_CENTER
            pivotY = height * PIVOT_CENTER

            val absPosition = abs(position)

            scaleX = calculateScale(absPosition)
            scaleY = calculateScale(absPosition)
            alpha = calculateAlpha(absPosition)
            translationY = calculateElevation(absPosition)
            rotation = calculateRotation(position)
            translationX = NO_TRANSLATION
        }
    }

    private fun calculateScale(absPosition: Float): Float {
        // Ensure center item is always at full scale
        return if (absPosition < CENTER_THRESHOLD) {
            MAX_SCALE
        } else {
            val scaleFactor = MIN_SCALE + (MAX_SCALE - MIN_SCALE) * (MAX_SCALE - absPosition).coerceIn(
                MIN_POSITION,
                MAX_POSITION
            )
            scaleFactor.coerceIn(MIN_SCALE, MAX_SCALE)
        }
    }

    private fun calculateAlpha(absPosition: Float): Float {
        return when {
            absPosition < CENTER_THRESHOLD -> MAX_ALPHA
            absPosition <= ALWAYS_VISIBLE_THRESHOLD -> MAX_ALPHA
            else -> {
                val alphaFactor =
                    MIN_ALPHA + (MAX_ALPHA - MIN_ALPHA) * (MAX_SCALE - absPosition).coerceIn(
                        MIN_POSITION,
                        MAX_POSITION
                    )
                alphaFactor.coerceIn(MIN_ALPHA, MAX_ALPHA)
            }
        }
    }

    private fun calculateElevation(absPosition: Float): Float {
        return ELEVATION_OFFSET * absPosition.coerceAtMost(MAX_POSITION)
    }

    private fun calculateRotation(position: Float): Float {
        // Don't rotate the center item (position close to 0)
        return if (abs(position) < CENTER_THRESHOLD) {
            0f
        } else {
            (position * MAX_ROTATION).coerceIn(-MAX_ROTATION, MAX_ROTATION)
        }
    }

    private companion object {
        private const val MIN_SCALE = 0.92f
        private const val MAX_SCALE = 1f
        private const val MIN_ALPHA = 0.90f
        private const val MAX_ALPHA = 1f
        private const val ELEVATION_OFFSET = 40f
        private const val MAX_ROTATION = 4f
        private const val PIVOT_CENTER = 0.5f
        private const val NO_TRANSLATION = 0f
        private const val MIN_POSITION = 0f
        private const val MAX_POSITION = 1f
        private const val ALWAYS_VISIBLE_THRESHOLD = 2f
        private const val CENTER_THRESHOLD = 0.1f
    }
}