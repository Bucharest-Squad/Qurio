package com.bucharest.qurio.presentation.game.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.bucharest.qurio.R
import com.bucharest.qurio.databinding.ComponentSecondaryActionButtonBinding

class SecondaryActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ComponentSecondaryActionButtonBinding =
        ComponentSecondaryActionButtonBinding.inflate(LayoutInflater.from(context), this)
    private var isCustomEnabled = true

    init {
        orientation = VERTICAL

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SecondaryActionButton)
            try {
                isCustomEnabled = typedArray.getBoolean(
                    R.styleable.SecondaryActionButton_secondaryButtonEnabled,
                    true
                )

                val textAttr =
                    context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.text))
                val textResId = textAttr.getResourceId(0, 0)
                val textValue = if (textResId != 0) {
                    context.getString(textResId)
                } else {
                    textAttr.getString(0) ?: context.getString(R.string.submit)
                }
                textAttr.recycle()

                setEnabledState(isCustomEnabled)
                binding.buttonText.text = textValue
            } finally {
                typedArray.recycle()
            }
        }
    }

    private fun setEnabledState(enabled: Boolean) {
        binding.buttonContainer.isEnabled = enabled
        binding.buttonText.isEnabled = enabled
        binding.buttonContainer.background =
            ContextCompat.getDrawable(context, R.drawable.secondary_button_bg)
        binding.buttonText.setTextColor(
            ContextCompat.getColor(context, R.color.primary)
        )
    }

    fun setButtonEnabled(enabled: Boolean) {
        isCustomEnabled = enabled
        setEnabledState(enabled)
    }

    fun setText(text: String) {
        binding.buttonText.text = text
    }

    fun getText(): String = binding.buttonText.text.toString()
}



