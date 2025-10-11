package com.bucharest.qurio.ui.homeScreen.components.carousel

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CarouselItemDecoration(
    private val spacing: Int = 16
) : RecyclerView.ItemDecoration() {
    
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        
        val position = parent.getChildAdapterPosition(view)
        
        // Add spacing between items
        if (position != RecyclerView.NO_POSITION) {
            outRect.right = spacing
        }
    }
}


