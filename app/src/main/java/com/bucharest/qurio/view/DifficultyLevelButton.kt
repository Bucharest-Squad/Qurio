package com.bucharest.qurio.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.bucharest.qurio.R

class DifficultyLevelButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        background = ContextCompat.getDrawable(context, R.drawable.difficulty_level_ripple_bg)
        isClickable = true
        isFocusable = true
        stateListAnimator = null
        gravity = android.view.Gravity.CENTER

        setTextAppearance(R.style.Label_Medium)
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DifficultyLevelButton,
            0, 0
        )
        try {
            val isInitiallySelected =
                typedArray.getBoolean(R.styleable.DifficultyLevelButton_isSelected, false)
            isSelected = isInitiallySelected
        } finally {
            typedArray.recycle()
        }
    }

    override fun setSelected(selected: Boolean) {
        if (this.isSelected == selected) return
        super.setSelected(selected)
        updateVisuals()
    }

    override fun performClick(): Boolean {
        isSelected = !isSelected
        return super.performClick()
    }

    private fun updateVisuals() {
        val textColor = if (isSelected) {
            ContextCompat.getColor(context, R.color.on_primary)
        } else {
            ContextCompat.getColor(context, R.color.shade_secondary)
        }
        setTextColor(textColor)
    }
}