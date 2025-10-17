package com.bucharest.qurio.presentation.game.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bucharest.qurio.R
import com.bucharest.qurio.databinding.ComponentQuestionNumberCardBinding

class QuestionNumberCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    
    private val binding: ComponentQuestionNumberCardBinding =
        ComponentQuestionNumberCardBinding.inflate(LayoutInflater.from(context), this)

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.QuestionNumberCard)

            val questionNumber = typedArray.getString(R.styleable.QuestionNumberCard_numberText) ?: ""

            typedArray.recycle()

            setQuestionNumber(questionNumber)
        }
    }

    fun setQuestionNumber(numberText: String) {
        binding.questionNumberText.text = numberText
    }
}
