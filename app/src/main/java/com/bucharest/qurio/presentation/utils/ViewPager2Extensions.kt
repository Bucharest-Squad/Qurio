package com.bucharest.qurio.presentation.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bucharest.qurio.presentation.constants.PresentationConstants

fun ViewPager2.configureCarousel() {
    orientation = ViewPager2.ORIENTATION_HORIZONTAL
    offscreenPageLimit = PresentationConstants.CAROUSEL_OFFSCREEN_PAGE_LIMIT
    
    (getChildAt(PresentationConstants.FIRST_CHILD_INDEX) as? RecyclerView)?.apply {
        clipToPadding = false
        setPadding(
            PresentationConstants.NO_PADDING, 
            PresentationConstants.NO_PADDING, 
            PresentationConstants.NO_PADDING, 
            PresentationConstants.NO_PADDING
        )
        overScrollMode = android.view.View.OVER_SCROLL_NEVER
        isNestedScrollingEnabled = true
    }
}
