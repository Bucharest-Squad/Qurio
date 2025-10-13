package com.bucharest.qurio.presentation.home

import com.bucharest.qurio.presentation.base.BaseView
import com.bucharest.qurio.presentation.home.state.StreakDayUiModel
import com.bucharest.qurio.presentation.home.state.CategoryUiModel
import com.bucharest.qurio.presentation.home.state.GameSessionUiModel

interface MainHomeView : BaseView {
    fun showUserStats(coins: Int, lives: Int, awards: Int)
    fun showStreak(currentStreak: Int, streakDays: List<StreakDayUiModel>)
    fun showCategories(categories: List<CategoryUiModel>)
    fun showRecentGames(games: List<GameSessionUiModel>)
    fun navigateToCategoryGame(categoryId: Int)
    fun navigateToAllGames()
    fun navigateToAllRecentGames()
}