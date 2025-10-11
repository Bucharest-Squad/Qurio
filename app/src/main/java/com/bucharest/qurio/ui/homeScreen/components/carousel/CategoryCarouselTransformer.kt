package com.bucharest.qurio.ui.homeScreen.components.carousel

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class CategoryCarouselTransformer : ViewPager2.PageTransformer {
    
    private companion object {
        private const val MIN_SCALE = 0.92f  // Slightly scale down side cards
        private const val MIN_ALPHA = 0.85f   // Slightly fade side cards
        private const val ELEVATION_OFFSET = 40f  // Lift UP the center card
        private const val MAX_ROTATION = 4f  // Maximum rotation angle in degrees (Z-axis tilt)
    }

    override fun transformPage(page: View, position: Float) {
        page.apply {
            // Set pivot point for rotation at center
            pivotX = width * 0.5f
            pivotY = height * 0.5f
            
            // Use absolute position for calculations
            val absPosition = abs(position)
            
            // Always keep items visible (don't hide them completely)
            // This prevents black screens and blinking
            when {
                absPosition >= 2 -> {
                    // Items very far away - still keep them visible but minimal
                    alpha = 0.3f
                    scaleX = MIN_SCALE
                    scaleY = MIN_SCALE
                    rotation = if (position > 0) MAX_ROTATION else -MAX_ROTATION
                    translationY = ELEVATION_OFFSET
                }
                else -> {
                    // Items in range [-2, 2] - smooth transitions
                    
                    // Smoothly scale down side cards (center card = 1.0, side cards = 0.92)
                    val scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - absPosition).coerceIn(0f, 1f)
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                    
                    // Smoothly fade side cards (center = 1.0, sides = 0.85)
                    // Keep minimum alpha higher to prevent blackness
                    val alphaFactor = MIN_ALPHA + (1 - MIN_ALPHA) * (1 - absPosition).coerceIn(0f, 1f)
                    alpha = alphaFactor.coerceAtLeast(0.5f)
                    
                    // Lift center card UP, push side cards down
                    translationY = ELEVATION_OFFSET * absPosition.coerceAtMost(1f)
                    
                    // Add Z-axis rotation (2D tilt) for side cards
                    // Left card (position = -1): rotate -4 degrees (tilt left)
                    // Center card (position = 0): no rotation 0 degrees
                    // Right card (position = +1): rotate +4 degrees (tilt right)
                    rotation = (position * MAX_ROTATION).coerceIn(-MAX_ROTATION, MAX_ROTATION)
                    
                    // NO horizontal translation - let ViewPager2 handle horizontal positioning
                    translationX = 0f
                }
            }
        }
    }
}


