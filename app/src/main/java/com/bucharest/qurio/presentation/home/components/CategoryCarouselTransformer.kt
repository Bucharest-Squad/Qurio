package com.bucharest.qurio.presentation.home.components

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class CategoryCarouselTransformer : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        page.apply {
            val absPosition = abs(position)


            translationY = if (absPosition < CENTER_THRESHOLD) {
                -CENTER_ELEVATION
            } else {
                SIDE_ELEVATION * absPosition
            }

            // Always apply horizontal spacing, even for center item
            translationX = position * ITEM_GAP
        }
    }

    private fun calculateScale(absPosition: Float): Float {
        // Center item at full scale, side items slightly smaller
        return if (absPosition < CENTER_THRESHOLD) {
            MAX_SCALE
        } else {
            // Smooth scale transition from center to sides
            MAX_SCALE - (absPosition * SCALE_REDUCTION)
        }
    }

    private fun calculateElevation(absPosition: Float): Float {
        return if (absPosition < CENTER_THRESHOLD) {
            // Center item - elevated (negative Y moves it up)
            -CENTER_ELEVATION
        } else {
            // Side items - progressively lower based on distance from center
            SIDE_ELEVATION * absPosition
        }
    }

    private companion object {
        private const val MAX_SCALE = 1f
        private const val MIN_SCALE = 0.92f  // Slight scale reduction for side items
        private const val SCALE_REDUCTION = 0.08f  // MAX_SCALE - MIN_SCALE

        private const val CENTER_ELEVATION = 50f   // How much center item is elevated
        private const val SIDE_ELEVATION = 30f     // How much side items are lowered

        private const val PIVOT_CENTER = 0.5f
        private const val CENTER_THRESHOLD = 0.1f
        private const val ITEM_GAP = 35f

    }
    }


