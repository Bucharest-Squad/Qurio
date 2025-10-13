package com.bucharest.qurio.presentation.home.state

data class HomeUiState(
    val streakDays: List<StreakDayUiState>
) {
    data class StreakDayUiState(
        val day: String,
        val isInStreak: Boolean
    )
}

