package com.bucharest.qurio.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.bucharest.qurio.databinding.ItemResultBinding

class ResultCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ItemResultBinding = ItemResultBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun setStars(count: Int) = with(binding) {
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

    fun setStats(correct: Int, incorrect: Int, skipped: Int,score:Int) = with(binding) {
        correctAnswersValue.text = correct.toString()
        incorrectAnswersValue.text = incorrect.toString()
        skippedAnswersValue.text = skipped.toString()
        resultCoinsValue.text = score.toString()
    }

    private fun dpToPx(dp: Int): Int =
        (dp * context.resources.displayMetrics.density).toInt()
}

