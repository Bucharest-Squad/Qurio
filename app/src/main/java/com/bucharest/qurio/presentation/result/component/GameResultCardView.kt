package com.bucharest.qurio.presentation.result.component

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bucharest.qurio.R
import com.bucharest.qurio.databinding.GameResultCardBinding

class GameResultCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = GameResultCardBinding.inflate(LayoutInflater.from(context), this)

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.GameResultCardView, 0, 0).apply {
            try {
                val resultText = getString(R.styleable.GameResultCardView_resultText)
                val blurVisible = getBoolean(R.styleable.GameResultCardView_blurVisible, true)
                val starsVisible = getBoolean(R.styleable.GameResultCardView_starsVisible, true)
                setResultText(resultText ?: "")
                showBlur(blurVisible)
                showStars(starsVisible)
            } finally {
                recycle()
            }
        }
    }

    fun updateResult(correctAnswers: Int, totalQuestions: Int) {
        if (totalQuestions == 0) return

        val ratio = correctAnswers.toFloat() / totalQuestions
        val stars = when {
            ratio >= 1f -> 3
            ratio >= 0.66f -> 2
            ratio >= 0.33f -> 1
            else -> 0
        }

        if (stars >= 1) {
            setResultText("Great job!")
            binding.txtResult.setOutlineColor(Color.parseColor("#1980B2"))
            binding.blurBackground.setImageResource(R.drawable.blur_background)
            binding.ribbon.setImageResource(R.drawable.ribbon)
        } else {
            setResultText("You lose")
            binding.txtResult.setOutlineColor(Color.parseColor("#E6311F"))
            binding.blurBackground.setImageResource(R.drawable.blur_bg_red)
            binding.ribbon.setImageResource(R.drawable.ribbon_red)
        }

        val starViews = listOf(binding.star1, binding.star2, binding.star3)
        starViews.forEachIndexed { index, star ->
            star.isVisible = index < stars
        }
        
        if (stars > 0) {
            animateStars()
        }
    }

    fun setResultText(text: String) {
        binding.txtResult.text = text
    }

    fun showBlur(show: Boolean) {
        binding.blurBackground.isVisible = show
    }

    fun setBlurBackground(resId: Int) {
        binding.blurBackground.setImageResource(resId)
    }

    fun animateStars() {
        val stars = listOf(binding.star1, binding.star2, binding.star3)

        stars.forEachIndexed { index, star ->
            star.scaleX = 0f
            star.scaleY = 0f
            star.alpha = 0f

            star.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setStartDelay((index * 200).toLong())
                .setDuration(400)
                .start()
        }
    }

    fun showStars(show: Boolean) {
        binding.starsContainer.isVisible = show
    }

    fun setReward(value: String) {
        binding.coinsReward.text = value
    }

    fun setCorrectCount(value: String) {
        binding.correctAnswersCount.text = value
    }

    fun setIncorrectCount(value: String) {
        binding.inCorrectAnswersCount.text = value
    }

    fun setSkippedCount(value: String) {
        binding.skippedAnswersCount.text = value
    }
}
