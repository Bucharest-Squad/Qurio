package com.bucharest.qurio.presentation.home

import com.bucharest.qurio.presentation.achievemetns_dialog.AchievementUImodel
import com.bucharest.qurio.presentation.base.BaseView
import com.bucharest.qurio.presentation.character_dialog.CharacterUiModel
import com.bucharest.qurio.presentation.home.state.CategoryUiModel
import com.bucharest.qurio.presentation.home.state.GameSessionUiModel
import com.bucharest.qurio.presentation.home.state.StreakDayUiModel

interface MainHomeView : BaseView {
    fun showUserStats(coins: Int, lives: Int, awards: Int)
    fun showStreak(currentStreak: Int, streakDays: List<StreakDayUiModel>)
    fun showCategories(categories: List<CategoryUiModel>)
    fun showRecentGames(games: List<GameSessionUiModel>)
    fun navigateToCategoryGame(categoryId: Int)
    fun navigateToAllGames()
    fun navigateToAllRecentGames()
    fun showSettingsDialog()
    fun showCharacterSelectionDialog(
        currentCharacterId: Int,
        charactersUiModel: List<CharacterUiModel>
    )

    fun showPurchaseLivesDialog()
    fun showAchievementsDialog(
        achievementUImodel: List<AchievementUImodel>
    )

    fun showCurrentCharacter(characterUiModel: CharacterUiModel)
    fun showAchievementsDialog()
}