package com.bucharest.qurio.presentation.home

import com.bucharest.qurio.presentation.base.BaseView

interface MainHomeView : BaseView {
    fun showUserStats(coins: Int, lives: Int, awards: Int)
    fun showStreak(currentStreak: Int, streakDays: List<StreakDayState>)
    fun showCategories(categories: List<CategoryState>)
    fun showRecentGames(games: List<GameSessionState>)
    fun navigateToCategoryGame(categoryId: Int)
    fun navigateToAllGames()
}

data class StreakDayState(
    val dayLabel: String,
    val isInStreak: Boolean
)

data class CategoryState(
    val id: Int,
    val title: String,
    val imageRes: Int,
    val startColor: Int,
    val endColor: Int
)

data class GameSessionState(
    val categoryName: String,
    val difficulty: String,
    val score: Int,
    val starsEarned: Int,
    val coinsEarned: Int,
    val durationSeconds: Int,
    val playedDate: String
)


