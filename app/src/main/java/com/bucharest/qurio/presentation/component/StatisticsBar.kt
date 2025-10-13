package com.bucharest.qurio.presentation.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bucharest.qurio.databinding.StatisticsBarBinding

class StatisticsBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    private val binding: StatisticsBarBinding =
        StatisticsBarBinding.inflate(LayoutInflater.from(context), this, true)

    fun setStats(
        livesCount: Int,
        pointsCount: Int,
        awardsCount: Int,
        isWinner: Boolean = false,
        onAddLivesClicked: () -> Unit,
        onNextArrowClicked: () -> Unit
    ) {
        updateStatsCounts(livesCount, pointsCount, awardsCount)
        updateWinnerCrown(isWinner)
        setupClickListeners(onAddLivesClicked, onNextArrowClicked)
    }

    private fun updateStatsCounts(livesCount: Int, pointsCount: Int, awardsCount: Int) {
        with(binding) {
            statisticsLivesCard.livesCount.text = livesCount.toString()
            statisticsPointsCard.pointsCount.text = pointsCount.toString()
            statisticsAwardsCard.awardsCount.text = awardsCount.toString()
        }
    }

    private fun updateWinnerCrown(isWinner: Boolean) {
        binding.statisticsPointsCard.icCrownImage.visibility = if (isWinner) VISIBLE else GONE
    }

    private fun setupClickListeners(
        onAddLivesClicked: () -> Unit,
        onNextArrowClicked: () -> Unit
    ) {
        with(binding) {
            statisticsLivesCard.addLiveButton.setOnClickListener { onAddLivesClicked() }
            statisticsAwardsCard.nextButton.setOnClickListener { onNextArrowClicked() }
        }
    }
}
