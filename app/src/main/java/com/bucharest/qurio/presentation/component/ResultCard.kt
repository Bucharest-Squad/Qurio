package com.bucharest.qurio.presentation.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.bucharest.qurio.databinding.ItemResultDialogBinding

class ResultCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private val binding: ItemResultDialogBinding = ItemResultDialogBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun setStats(
        correctCount: Int,
        incorrectCount: Int,
        skippedCount: Int,
        score: Int,
        starsCount: Int
    ) {
        with(binding) {
            correctAnswersValue.text = correctCount.toString()
            incorrectAnswersValue.text = incorrectCount.toString()
            skippedAnswersValue.text = skippedCount.toString()
            resultCoinsValue.text = score.toString()
        }
        configureStarsDisplay(starsCount)
    }

    private fun configureStarsDisplay(count: Int) {
        when (count) {
            SINGLE_STAR -> configureSingleStar()
            TWO_STARS -> configureTwoStars()
            THREE_STARS -> configureThreeStars()
            else -> hideAllStars()
        }
    }

    private fun configureSingleStar() {
        with(binding) {
            firstResultStar.visibility = GONE
            thirdResultStar.visibility = GONE
            secondResultStar.visibility = VISIBLE
            secondResultStar.updateTopMargin(SINGLE_STAR_TOP_MARGIN_DP)
        }
    }

    private fun configureTwoStars() {
        with(binding) {
            firstResultStar.visibility = VISIBLE
            thirdResultStar.visibility = VISIBLE
            secondResultStar.visibility = GONE
        }
    }

    private fun configureThreeStars() {
        with(binding) {
            firstResultStar.visibility = VISIBLE
            secondResultStar.visibility = VISIBLE
            thirdResultStar.visibility = VISIBLE
            secondResultStar.updateTopMargin(THREE_STARS_TOP_MARGIN_DP)
        }
    }

    private fun hideAllStars() {
        with(binding) {
            firstResultStar.visibility = GONE
            secondResultStar.visibility = GONE
            thirdResultStar.visibility = GONE
        }
    }

    private fun android.view.View.updateTopMargin(marginDp: Int) {
        updateLayoutParams<MarginLayoutParams> {
            topMargin = dpToPx(marginDp)
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    companion object {
        private const val SINGLE_STAR = 1
        private const val TWO_STARS = 2
        private const val THREE_STARS = 3
        private const val SINGLE_STAR_TOP_MARGIN_DP = 72
        private const val THREE_STARS_TOP_MARGIN_DP = 64
    }
}
