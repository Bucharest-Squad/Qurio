package com.bucharest.qurio.component

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

    fun setStats(lives: Int, points: Int, awards: Int, isWinner: Boolean = false) {
        binding.statisticsLivesCard.livesCount.text = lives.toString()
        binding.statisticsPointsCard.pointsCount.text = points.toString()
        binding.statisticsAwardsCard.awardsCount.text = awards.toString()
        binding.statisticsPointsCard.icCrownImage.visibility = if (isWinner) VISIBLE else GONE
    }
}
