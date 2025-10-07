package com.bucharest.ui.homeScreen

data class HomeUiState (
    val streakDays:List<StreakDayUiState>

){
    data class StreakDayUiState (
        val day:String,
        val isSelected:Boolean
    )
}