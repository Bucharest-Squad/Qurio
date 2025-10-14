package com.bucharest.qurio.presentation.home

import android.content.Context
import com.bucharest.qurio.domain.entity.Category
import com.bucharest.qurio.domain.entity.Character
import com.bucharest.qurio.domain.entity.GameSession
import com.bucharest.qurio.domain.entity.User
import com.bucharest.qurio.domain.repository.CategoryRepository
import com.bucharest.qurio.domain.repository.CharacterRepository
import com.bucharest.qurio.domain.repository.GameRepository
import com.bucharest.qurio.domain.repository.UserRepository
import com.bucharest.qurio.presentation.base.BasePresenter
import com.bucharest.qurio.presentation.character_dialog.CharacterMapper
import com.bucharest.qurio.presentation.character_dialog.CharacterUiModel
import com.bucharest.qurio.presentation.home.mapper.CategoryMapper
import com.bucharest.qurio.presentation.home.state.StreakDayUiModel
import com.bucharest.qurio.presentation.home.state.GameSessionUiModel
import kotlinx.datetime.*
import kotlin.time.Duration.Companion.milliseconds

class MainHomePresenter(
    private val userRepository: UserRepository,
    private val gameRepository: GameRepository,
    private val characterRepository: CharacterRepository,
    private val categoryRepository: CategoryRepository,
    private val context: Context
) : BasePresenter<MainHomeView>() {

    override fun onViewAttached() {
        super.onViewAttached()
        loadHomeData()
    }

    fun onRefresh() {
        loadHomeData()
        //get
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

    fun updateCurrentCharacter(characterId:Int){
        tryToExecute(
            execute = { userRepository.setActiveCharacter(characterId) },
            onSuccess = {},
            onError =::handleHomeDataError,
            onStart = { executeIfViewAttached { showLoading() } },
            onFinally = { executeIfViewAttached { hideLoading() } }
        )

    }
    fun onBuyClicked(characterId:Int){
        tryToExecute(
            execute = { characterRepository.unlockCharacter(characterId) },
            onSuccess = {},
            onError =::handleHomeDataError,
            onStart = { executeIfViewAttached { showLoading() } },
            onFinally = { executeIfViewAttached { hideLoading() } }
        )

    }
    fun onCharacterClicked() {
        tryToExecute(
            execute = { userRepository.getUser().currentCharacterId },
            onSuccess = ::setCurrentCharacter,
            onError = ::handleHomeDataError ,
            onStart = { executeIfViewAttached { showLoading() } },
            onFinally = { executeIfViewAttached { hideLoading() } }
        )

    }
    fun setCurrentCharacter(id: Int){
        tryToExecute(
            execute = {characterRepository.getAllCharacters() },
            onSuccess = {characters->
                executeIfViewAttached {
                    showCharacterSelectionDialog(id, characters.map { CharacterMapper.mapCharacterToUiState(it) })
                }
            },
            onError = ::handleHomeDataError ,
            onStart = { executeIfViewAttached { showLoading() } },
            onFinally = { executeIfViewAttached { hideLoading() } }
        )

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
        val recentGames = gameRepository.getRecentSessions(limit = RECENT_GAMES_LIMIT)
        val currentCharacter = characterRepository.getCurrentCharacter(user.currentCharacterId)

        return HomeData(user, categories, recentGames,currentCharacter)
    }
    
    private fun handleHomeDataSuccess(homeData: HomeData) {
        executeIfViewAttached {
            displayCurrentCharacter(homeData.currentCharacter)
            displayUserStats(homeData.user, homeData.recentGames)
            displayStreak(homeData.user)
            displayCategories(homeData.categories)
            displayRecentGames(homeData.recentGames)
        }
    }
    
    private fun handleHomeDataError(throwable: Throwable) {
        executeIfViewAttached {
            showError(throwable.message ?: ERROR_LOADING_DATA)
        }
    }
    
    private fun displayUserStats(user: User, recentGames: List<GameSession>) {
        val totalAwards = calculateTotalAwards(recentGames)
        executeIfViewAttached {
            showUserStats(
                coins = user.coins,
                lives = user.lives,
                awards = totalAwards
            )
        }
    }
    private fun displayCurrentCharacter(character: Character) {
        val characterUiModel = CharacterMapper.mapCharacterToUiState(character)

        executeIfViewAttached {
           showCurrentCharacter(characterUiModel)
        }
    }
    
    private fun calculateTotalAwards(recentGames: List<GameSession>): Int {
        return recentGames.sumOf { it.starsEarned }
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
            DEFAULT_DURATION_SECONDS
        }
    }
    
    private fun formatPlayedDate(session: GameSession): String {
        val instant = Instant.fromEpochMilliseconds(session.startedAt.toEpochMilli())
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return formatDate(localDateTime)
    }
    
    private fun formatDate(dateTime: LocalDateTime): String {
        val day = dateTime.dayOfMonth.toString().padStart(DATE_PADDING_LENGTH, DATE_PADDING_CHAR)
        val month = dateTime.monthNumber.toString().padStart(DATE_PADDING_LENGTH, DATE_PADDING_CHAR)
        val year = dateTime.year
        return "$day-$month-$year"
    }

    private fun generateStreakDays(currentStreak: Int): List<StreakDayUiModel> {
        val dayLabels = listOf("S", "M", "T", "W", "T", "F", "S")
        
        return dayLabels.mapIndexed { index, label ->
            StreakDayUiModel(label, isDayInStreak(index, currentStreak))
        }
    }
    
    private fun isDayInStreak(dayIndex: Int, currentStreak: Int): Boolean {
        return when {
            currentStreak == NO_STREAK -> false
            currentStreak >= DAYS_IN_WEEK -> true
            else -> dayIndex < currentStreak
        }
    }

    private data class HomeData(
        val user: User,
        val categories: List<Category>,
        val recentGames: List<GameSession>,
        val currentCharacter: Character
    )

    companion object {
        private const val RECENT_GAMES_LIMIT = 5
        private const val DEFAULT_DURATION_SECONDS = 0
        private const val DATE_PADDING_LENGTH = 2
        private const val DATE_PADDING_CHAR = '0'
        private const val NO_STREAK = 0
        private const val DAYS_IN_WEEK = 7
        private const val ERROR_LOADING_DATA = "Failed to load home data"
    }
}
