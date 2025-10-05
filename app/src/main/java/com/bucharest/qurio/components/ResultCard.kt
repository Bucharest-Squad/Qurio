package com.bucharest.qurio.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.bucharest.qurio.R
import com.bucharest.qurio.databinding.ItemResultBinding

class ResultCard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ItemResultBinding = ItemResultBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    init {
        setStars(3)
        setStats(0, 0, 0)
    }

    private fun setStars(count: Int) = with(binding) {
        when (count) {
            1 -> {
                findViewById<ImageView>(R.id.first_result_star).visibility = GONE
                findViewById<ImageView>(R.id.third_result_star).visibility = GONE
                findViewById<ImageView>(R.id.second_result_star).visibility = VISIBLE
                findViewById<ImageView>(R.id.second_result_star).updateLayoutParams<MarginLayoutParams> {
                    topMargin = dpToPx(72)
                }
            }

            2 -> {
                 findViewById<ImageView>(R.id.first_result_star).visibility = VISIBLE
                 findViewById<ImageView>(R.id.third_result_star).visibility = VISIBLE
                 findViewById<ImageView>(R.id.second_result_star).visibility = GONE
            }

            3 -> {
                 findViewById<ImageView>(R.id.first_result_star).visibility = VISIBLE
                 findViewById<ImageView>(R.id.second_result_star).visibility = VISIBLE
                 findViewById<ImageView>(R.id.third_result_star).visibility = VISIBLE
                 findViewById<ImageView>(R.id.second_result_star).updateLayoutParams<MarginLayoutParams> {
                    topMargin = dpToPx(64)
                }
            }

            else -> {
                 findViewById<ImageView>(R.id.first_result_star).visibility = GONE
                 findViewById<ImageView>(R.id.second_result_star).visibility = GONE
                 findViewById<ImageView>(R.id.third_result_star).visibility = GONE
            }
        }
    }

    fun setStats(correct: Int, incorrect: Int, skipped: Int) = with(binding) {
        findViewById<TextView>(R.id.correctAnswersValue).text = correct.toString()
        findViewById<TextView>(R.id.incorrectAnswersValue).text = incorrect.toString()
        findViewById<TextView>(R.id.skippedAnswersValue).text = skipped.toString()
    }

    private fun dpToPx(dp: Int): Int = (dp * context.resources.displayMetrics.density).toInt()
}

