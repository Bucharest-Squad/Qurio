package com.bucharest.qurio.presentation.game.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bucharest.qurio.R
import com.bucharest.qurio.databinding.ComponentQuestionCardBinding

class QuestionCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    
    private val binding: ComponentQuestionCardBinding =
        ComponentQuestionCardBinding.inflate(LayoutInflater.from(context), this)

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.QuestionCardView)

            val question = typedArray.getString(R.styleable.QuestionCardView_questionText) ?: ""
            val number = typedArray.getString(R.styleable.QuestionCardView_questionNumber) ?: ""

            typedArray.recycle()

            setQuestion(question)
            setQuestionNumber(number)
        }
    }

    fun setQuestion(question: String) {
        binding.questionText.text = question
    }

    fun setQuestionNumber(numberText: String) {
        binding.questionNumberCard.setQuestionNumber(numberText)
    }
}



