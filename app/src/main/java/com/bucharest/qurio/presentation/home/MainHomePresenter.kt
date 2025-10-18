package com.bucharest.qurio.presentation.home

import android.content.Context
import com.bucharest.qurio.R
import com.bucharest.qurio.domain.entity.Achievement
import com.bucharest.qurio.domain.entity.Category
import com.bucharest.qurio.domain.entity.Character
import com.bucharest.qurio.domain.entity.GameSession
import com.bucharest.qurio.domain.entity.User
import com.bucharest.qurio.audio.AudioManager
import com.bucharest.qurio.domain.repository.AchievementRepository
import com.bucharest.qurio.domain.repository.CategoryRepository
import com.bucharest.qurio.domain.repository.CharacterRepository
import com.bucharest.qurio.domain.repository.GameRepository
import com.bucharest.qurio.domain.repository.UserRepository
import com.bucharest.qurio.presentation.achievemetns_dialog.AchievementMapper
import com.bucharest.qurio.presentation.base.BasePresenter
import com.bucharest.qurio.presentation.character_dialog.CharacterMapper
import com.bucharest.qurio.presentation.character_dialog.CharacterUiModel
import com.bucharest.qurio.presentation.constants.PresentationConstants
import com.bucharest.qurio.presentation.home.mapper.CategoryMapper
import com.bucharest.qurio.presentation.utils.NetworkUtils
import com.bucharest.qurio.presentation.home.state.GameSessionUiModel
import com.bucharest.qurio.presentation.home.state.StreakDayUiModel
import com.bucharest.qurio.presentation.utils.DateUtils
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.milliseconds

class MainHomePresenter(
    private val userRepository: UserRepository,
    private val gameRepository: GameRepository,
    private val characterRepository: CharacterRepository,
    private val achievementRepository: AchievementRepository,
    private val categoryRepository: CategoryRepository,
    private val audioManager: AudioManager,
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
        audioManager.playButtonPress()
        checkLivesAndNavigate(categoryId)
    }
    
    private fun checkLivesAndNavigate(categoryId: Int) {
        tryToExecute(
            execute = { userRepository.getUser().lives },
            onSuccess = { lives ->
                executeIfViewAttached {
                    if (lives > 0) {
                        navigateToCategoryGame(categoryId)
                    } else {
                        showPurchaseLivesDialog()
                    }
                }
            },
            onError = { 
                executeIfViewAttached {
                    showError("Failed to check user lives")
                }
            }
        )
    }

    fun onViewAllClicked() {
        audioManager.playButtonPress()
        executeIfViewAttached {
            navigateToAllGames()
        }
    }
    
    fun onViewAllRecentGamesClicked() {
        audioManager.playButtonPress()
        executeIfViewAttached {
            navigateToAllRecentGames()
        }
    }
    
    fun onSettingsClicked() {
        audioManager.playButtonPress()
        executeIfViewAttached {
            showSettingsDialog()
        }
    }

    fun onBuyLifeClicked() {
        audioManager.playButtonPress()
        executeIfViewAttached {
            showPurchaseLivesDialog()
        }
    }

    fun updateCurrentCharacter(characterId: Int) {
        audioManager.playCharacterSelect()
        tryToExecute(
            execute = { userRepository.setActiveCharacter(characterId) },
            onSuccess = { 
                loadHomeDataWithoutLoading()
            },
            onError = ::handleHomeDataError,
            onStart = { },
            onFinally = { }
        )
    }

    fun onBuyClicked(characterId: Int) {
        audioManager.playButtonPress()
        tryToExecute(
            execute = { characterRepository.unlockCharacter(characterId) },
            onSuccess = { 
                loadHomeDataWithoutLoading()
                refreshCharacterData()
            },
            onError = { error ->
                executeIfViewAttached { 
                    showError("Purchase failed: ${error.message}")
                }
            },
            onStart = { executeIfViewAttached { showLoading() } },
            onFinally = { executeIfViewAttached { hideLoading() } }
        )
    }
    
    fun refreshCharacterData() {
        tryToExecute(
            execute = { 
                val characters = characterRepository.getAllCharacters()
                val user = userRepository.getUser()
                Pair(characters, user.coins)
            },
            onSuccess = { (characters, userCoins) ->
                executeIfViewAttached {
                    refreshCharacterSelectionDialog(characters.map { CharacterMapper.mapCharacterToUiState(it, userCoins) })
                }
            },
            onError = ::handleHomeDataError,
            onStart = { },
            onFinally = { }
        )
    }

    fun onCharacterClicked() {
        audioManager.playButtonPress()
        tryToExecute(
            execute = { userRepository.getUser().currentCharacterId },
            onSuccess = ::setCurrentCharacter,
            onError = ::handleHomeDataError,
            onStart = { },
            onFinally = { }
        )
    }

    fun setCurrentCharacter(id: Int) {
        tryToExecute(
            execute = { 
                val characters = characterRepository.getAllCharacters()
                val user = userRepository.getUser()
                Pair(characters, user.coins)
            },
            onSuccess = { (characters, userCoins) ->
                executeIfViewAttached {
                    showCharacterSelectionDialog(id, characters.map { CharacterMapper.mapCharacterToUiState(it, userCoins) })
                }
            },
            onError = ::handleHomeDataError,
            onStart = { },
            onFinally = { }
        )
    }
    
    fun onPurchaseLivesClicked() {
        audioManager.playButtonPress()
        executeIfViewAttached {
            showPurchaseLivesDialog()
        }
    }
    
    fun onAchievementsClicked() {
        audioManager.playButtonPress()
        tryToExecute(
            execute = { achievementRepository.getAllAchievements() },
            onSuccess = { achievements ->
                executeIfViewAttached {
                    showAchievementsDialog(
                        achievements.map {
                            AchievementMapper.mapAchievementToUiModel(it, context)
                        }
                    )
                }
            },
            onError = {},
            onStart = {},
            onFinally = {}
        )
    }
    
    fun onLastGameClicked(game: GameSessionUiModel) {
        audioManager.playButtonPress()
        executeIfViewAttached {
            showMessage("Game: ${game.categoryName} - ${game.score} pts")
        }
    }

    // Your custom onBuyLife method for the buy life dialog
    fun onBuyLife() {
        tryToExecute(
            execute = { 
                val user = userRepository.getUser()
                if (user.coins < 200) {
                    throw Exception("Not enough coins! You need 200 coins to buy a life.")
                }
                userRepository.updateCoins(-200) // Deduct coins first
                userRepository.updateLives(1) // Then add life
            },
            onSuccess = { 
                executeIfViewAttached {
                    loadUserStatsOnly()
                }
            },
            onError = { throwable ->
                executeIfViewAttached {
                    showError(throwable.message ?: "Failed to buy life")
                }
            }
        )
    }

    fun loadHomeData() {
        if (!NetworkUtils.isConnectedToInternet(context)) {
            executeIfViewAttached { showError("No internet connection") }
            return
        }
        
        tryToExecute(
            execute = ::fetchAllHomeData,
            onSuccess = ::handleHomeDataSuccess,
            onError = ::handleHomeDataError,
            onStart = { executeIfViewAttached { showLoading() } },
            onFinally = { executeIfViewAttached { hideLoading() } }
        )
    }
    
    private fun loadHomeDataWithoutLoading() {
        if (!NetworkUtils.isConnectedToInternet(context)) {
            executeIfViewAttached { showError("No internet connection") }
            return
        }
        
        tryToExecute(
            execute = ::fetchAllHomeData,
            onSuccess = ::handleHomeDataSuccess,
            onError = ::handleHomeDataError,
            onStart = { },
            onFinally = { }
        )
    }
    
    private suspend fun fetchAllHomeData(): HomeData {
        val user = userRepository.getUser()
        val categories = categoryRepository.getAllCategories().shuffled()
        val recentGames = gameRepository.getRecentSessions(limit = PresentationConstants.RECENT_GAMES_LIMIT)
        val currentCharacter = characterRepository.getCurrentCharacter(user.currentCharacterId)
        val achievements = achievementRepository.getUnlockedAchievements()
        
        return HomeData(user, categories, recentGames, currentCharacter, achievements)
    }
    
    private fun handleHomeDataSuccess(homeData: HomeData) {
        executeIfViewAttached {
            displayCurrentCharacter(homeData.currentCharacter, homeData.user.coins)
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
    
    private fun displayUserStats(user: User, achievements: List<Achievement>) {
        executeIfViewAttached {
            showUserStats(
                coins = user.coins,
                lives = user.lives,
                awards = achievements.size
            )
        }
    }

    // Your custom method for updating only user stats (to avoid blinking)
    private fun loadUserStatsOnly() {
        tryToExecute(
            execute = {
                val user = userRepository.getUser()
                val achievements = achievementRepository.getUnlockedAchievements()
                Pair(user, achievements)
            },
            onSuccess = { (user, achievements) ->
                displayUserStats(user, achievements)
            },
            onError = { throwable ->
                executeIfViewAttached {
                    showError("Failed to update user stats")
                }
            }
        )
    }

    private fun displayCurrentCharacter(character: Character, userCoins: Int) {
        val characterUiModel = CharacterMapper.mapCharacterToUiState(character, userCoins)
        executeIfViewAttached {
            showCurrentCharacter(characterUiModel)
        }
    }
    
    private fun displayStreak(user: User) {
        val streakDays = generateStreakDaysWithOffset(user.currentDailyStreak, user.streakStartDate)
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
    
    private fun generateStreakDaysWithOffset(currentStreak: Int, streakStartDate: LocalDate?): List<StreakDayUiModel> {
        val dayLabels = listOf("S", "M", "T", "W", "Th", "F", "S")
        
        val kotlinDayOrdinal = if (streakStartDate != null) {
            streakStartDate.dayOfWeek.ordinal
        } else {
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).dayOfWeek.ordinal
        }
        
        val startDayOfWeek = when (kotlinDayOrdinal) {
            0 -> 1
            1 -> 2
            2 -> 3
            3 -> 4  
            4 -> 5
            5 -> 6
            6 -> 0
            else -> 0
        }
        
        val result = dayLabels.mapIndexed { index, label ->
            val isInStreak = isDayInStreakRange(index, startDayOfWeek, currentStreak)
            StreakDayUiModel(label, isInStreak)
        }
        
        return result
    }
    
    private fun isDayInStreak(dayIndex: Int, currentStreak: Int): Boolean {
        return when {
            currentStreak == PresentationConstants.NO_STREAK -> false
            currentStreak >= PresentationConstants.DAYS_IN_WEEK -> true
            else -> dayIndex < currentStreak
        }
    }
    
    private fun isDayInStreakRange(dayIndex: Int, startDayOfWeek: Int, currentStreak: Int): Boolean {
        return when {
            currentStreak == PresentationConstants.NO_STREAK -> false
            currentStreak >= PresentationConstants.DAYS_IN_WEEK -> true
            else -> {
                val daysFromStart = (dayIndex - startDayOfWeek + 7) % 7
                daysFromStart < currentStreak
            }
        }
    }

    fun getStreakDescription(currentStreak: Int): String {
        return when {
            currentStreak > PresentationConstants.MIN_ACTIVE_STREAK -> context.getString(R.string.streak_description_active)
            else -> context.getString(R.string.streak_description_inactive)
        }
    }

    private data class HomeData(
        val user: User,
        val categories: List<Category>,
        val recentGames: List<GameSession>,
        val currentCharacter: Character,
        val achievements: List<Achievement>
    )
}