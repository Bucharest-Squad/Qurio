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
        binding.statisticsLivesCard.livesCount.text = livesCount.toString()
        binding.statisticsPointsCard.pointsCount.text = pointsCount.toString()
        binding.statisticsAwardsCard.awardsCount.text = awardsCount.toString()
        binding.statisticsPointsCard.icCrownImage.visibility = if (isWinner) VISIBLE else GONE
        binding.statisticsLivesCard.addLiveButton.setOnClickListener { onAddLivesClicked() }
        binding.statisticsAwardsCard.nextButton.setOnClickListener { onNextArrowClicked() }
    }
}
