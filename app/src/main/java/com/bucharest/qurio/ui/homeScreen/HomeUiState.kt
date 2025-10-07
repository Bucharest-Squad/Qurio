package com.bucharest.qurio.ui.homeScreen

data class HomeUiState (
    val streakDays:List<StreakDayUiState>

){
    data class StreakDayUiState (
        val day:String,
        val isInStreak:Boolean
    )
}