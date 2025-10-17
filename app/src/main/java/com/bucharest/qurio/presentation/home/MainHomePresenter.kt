package com.bucharest.qurio.presentation.home

import android.content.Context
import com.bucharest.qurio.domain.entity.Category
import com.bucharest.qurio.domain.entity.GameSession
import com.bucharest.qurio.domain.entity.User
import com.bucharest.qurio.domain.repository.CategoryRepository
import com.bucharest.qurio.domain.repository.GameRepository
import com.bucharest.qurio.domain.repository.UserRepository
import com.bucharest.qurio.domain.repository.AchievementRepository
import com.bucharest.qurio.presentation.base.BasePresenter
import com.bucharest.qurio.presentation.constants.PresentationConstants
import com.bucharest.qurio.presentation.home.mapper.CategoryMapper
import com.bucharest.qurio.presentation.home.state.StreakDayUiModel
import com.bucharest.qurio.presentation.home.state.GameSessionUiModel
import com.bucharest.qurio.presentation.utils.DateUtils
import kotlinx.datetime.*
import kotlin.time.Duration.Companion.milliseconds

class MainHomePresenter(
    private val userRepository: UserRepository,
    private val gameRepository: GameRepository,
    private val categoryRepository: CategoryRepository,
    private val achievementRepository: AchievementRepository,
    private val context: Context
) : BasePresenter<MainHomeView>() {

    override fun onViewAttached() {
        super.onViewAttached()
        loadHomeData()
    }

    fun onRefresh() {
        loadHomeData()
    }
    
    fun onCategoryClicked(categoryId: Int) {
        executeIfViewAttached {
            navigateToCategoryGame(categoryId)
        }
    }

    fun onViewAllClicked() {
        executeIfViewAttached {
            navigateToAllGames()
        }
    }
    
    fun onViewAllRecentGamesClicked() {
        executeIfViewAttached {
            navigateToAllRecentGames()
        }
    }
    
    fun onSettingsClicked() {
        executeIfViewAttached {
            showSettingsDialog()
        }
    }
    
    fun onCharacterClicked() {
        executeIfViewAttached {
            showCharacterSelectionDialog()
        }
    }
    
    fun onPurchaseLivesClicked() {
        executeIfViewAttached {
            showPurchaseLivesDialog()
        }
    }
    
    fun onAchievementsClicked() {
        executeIfViewAttached {
            showAchievementsDialog()
        }
    }
    
    fun onLastGameClicked(game: GameSessionUiModel) {
        executeIfViewAttached {
            showMessage("Game: ${game.categoryName} - ${game.score} pts")
        }
    }

    fun loadHomeData() {
        tryToExecute(
            execute = ::fetchAllHomeData,
            onSuccess = ::handleHomeDataSuccess,
            onError = ::handleHomeDataError,
            onStart = { executeIfViewAttached { showLoading() } },
            onFinally = { executeIfViewAttached { hideLoading() } }
        )
    }
    
    private suspend fun fetchAllHomeData(): HomeData {
        val user = userRepository.getUser()
        val categories = categoryRepository.getAllCategories().shuffled()
        val recentGames = gameRepository.getRecentSessions(limit = PresentationConstants.RECENT_GAMES_LIMIT)
        val achievements = achievementRepository.getUnlockedAchievements()
        
        return HomeData(user, categories, recentGames, achievements)
    }
    
    private fun handleHomeDataSuccess(homeData: HomeData) {
        executeIfViewAttached {
            displayUserStats(homeData.user, homeData.achievements)
            displayStreak(homeData.user)
            displayCategories(homeData.categories)
            displayRecentGames(homeData.recentGames)
        }
    }
    
    private fun handleHomeDataError(throwable: Throwable) {
        executeIfViewAttached {
            showError(throwable.message ?: PresentationConstants.ERROR_LOADING_DATA)
        }
    }
    
    private fun displayUserStats(user: User, achievements: List<com.bucharest.qurio.domain.entity.Achievement>) {
        executeIfViewAttached {
            showUserStats(
                coins = user.coins,
                lives = user.lives,
                awards = achievements.size
            )
        }
    }
    
    private fun displayStreak(user: User) {
        val streakDays = generateStreakDays(user.currentDailyStreak)
        executeIfViewAttached {
            showStreak(user.currentDailyStreak, streakDays)
        }
    }
    
    private fun displayCategories(categories: List<Category>) {
        val categoryStates = CategoryMapper.toUiState(categories, context)
        executeIfViewAttached {
            showCategories(categoryStates)
        }
    }
    
    private fun displayRecentGames(recentGames: List<GameSession>) {
        val gameStates = mapGameSessionsToStates(recentGames)
        executeIfViewAttached {
            showRecentGames(gameStates)
        }
    }
    
    private fun mapGameSessionsToStates(sessions: List<GameSession>): List<GameSessionUiModel> {
        return sessions.map { session -> mapGameSessionToState(session) }
    }
    
    private fun mapGameSessionToState(session: GameSession): GameSessionUiModel {
        return GameSessionUiModel(
            categoryName = session.category.name,
            difficulty = session.difficulty.name,
            score = session.totalScore,
            starsEarned = session.starsEarned,
            coinsEarned = session.coinsEarned,
            durationSeconds = calculateDuration(session),
            playedDate = formatPlayedDate(session)
        )
    }
    
    private fun calculateDuration(session: GameSession): Int {
        return if (session.finishedAt != null) {
            val startMillis = session.startedAt.toEpochMilli()
            val endMillis = session.finishedAt.toEpochMilli()
            ((endMillis - startMillis).milliseconds.inWholeSeconds).toInt()
        } else {
            PresentationConstants.DEFAULT_DURATION_SECONDS
        }
    }
    
    private fun formatPlayedDate(session: GameSession): String {
        val instant = Instant.fromEpochMilliseconds(session.startedAt.toEpochMilli())
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return DateUtils.formatDate(localDateTime)
    }

    private fun generateStreakDays(currentStreak: Int): List<StreakDayUiModel> {
        val dayLabels = listOf("S", "M", "T", "W", "T", "F", "S")
        
        return dayLabels.mapIndexed { index, label ->
            StreakDayUiModel(label, isDayInStreak(index, currentStreak))
        }
    }
    
    private fun isDayInStreak(dayIndex: Int, currentStreak: Int): Boolean {
        return when {
            currentStreak == PresentationConstants.NO_STREAK -> false
            currentStreak >= PresentationConstants.DAYS_IN_WEEK -> true
            else -> dayIndex < currentStreak
        }
    }

    private data class HomeData(
        val user: User,
        val categories: List<Category>,
        val recentGames: List<GameSession>,
        val achievements: List<com.bucharest.qurio.domain.entity.Achievement>
    )

}
