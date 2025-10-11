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

    private fun setStars(count: Int) = with(binding) {
        when (count) {
            1 -> {
                firstResultStar.visibility = GONE
                thirdResultStar.visibility = GONE
                secondResultStar.visibility = VISIBLE
                secondResultStar.updateLayoutParams<MarginLayoutParams> {
                    topMargin = dpToPx(72)
                }
            }

            2 -> {
                firstResultStar.visibility = VISIBLE
                thirdResultStar.visibility = VISIBLE
                secondResultStar.visibility = GONE
            }

            3 -> {
                firstResultStar.visibility = VISIBLE
                secondResultStar.visibility = VISIBLE
                thirdResultStar.visibility = VISIBLE
                secondResultStar.updateLayoutParams<MarginLayoutParams> {
                    topMargin = dpToPx(64)
                }
            }

            else -> {
                firstResultStar.visibility = GONE
                secondResultStar.visibility = GONE
                thirdResultStar.visibility = GONE
            }
        }
    }

    fun setStats(
        correctCount: Int,
        incorrectCount: Int,
        skippedCount: Int,
        score: Int,
        starsCount:Int
    ) = with(binding) {
        correctAnswersValue.text = correctCount.toString()
        incorrectAnswersValue.text = incorrectCount.toString()
        skippedAnswersValue.text = skippedCount.toString()
        resultCoinsValue.text = score.toString()
        setStars(starsCount)
    }

    private fun dpToPx(dp: Int): Int =
        (dp * context.resources.displayMetrics.density).toInt()
}

