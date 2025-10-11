package com.bucharest.qurio.presentation.home

import android.content.Context
import android.util.Log
import com.bucharest.qurio.domain.repository.CategoryRepository
import com.bucharest.qurio.domain.repository.GameRepository
import com.bucharest.qurio.domain.repository.UserRepository
import com.bucharest.qurio.presentation.base.BasePresenter
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class MainHomePresenter(
    private val userRepository: UserRepository,
    private val gameRepository: GameRepository,
    private val categoryRepository: CategoryRepository,
    private val context: Context
) : BasePresenter<MainHomeView>() {

    override fun onViewAttached() {
        super.onViewAttached()
        Log.d(TAG, "View attached, loading home data...")
        loadHomeData()
    }

    fun loadHomeData() {
        Log.d(TAG, "Starting to load home data...")
        tryToExecute(
            execute = {
                Log.d(TAG, "Fetching user data...")
                val user = userRepository.getUser()
                Log.d(TAG, "User: coins=${user.coins}, lives=${user.lives}, streak=${user.currentDailyStreak}")
                
                Log.d(TAG, "Fetching categories...")
                val categories = categoryRepository.getAllCategories()
                Log.d(TAG, "Found ${categories.size} categories")
                
                Log.d(TAG, "Fetching recent games...")
                val recentGames = gameRepository.getRecentSessions(limit = 5)
                Log.d(TAG, "Found ${recentGames.size} recent games")
                
                Triple(user, categories, recentGames)
            },
            onSuccess = { (user, categories, recentGames) ->
                Log.d(TAG, "Data loaded successfully!")
                executeIfViewAttached {
                    // Show user stats
                    Log.d(TAG, "Showing user stats...")
                    showUserStats(
                        coins = user.coins,
                        lives = user.lives,
                        awards = recentGames.sumOf { it.starsEarned }
                    )

                    // Show streak
                    Log.d(TAG, "Showing streak...")
                    val streakDays = generateStreakDays(user.currentDailyStreak)
                    showStreak(user.currentDailyStreak, streakDays)

                    // Show categories with proper UI mapping
                    Log.d(TAG, "Mapping ${categories.size} categories to UI...")
                    val categoryStates = CategoryMapper.toUiState(categories, context)
                    Log.d(TAG, "Mapped to ${categoryStates.size} UI categories")
                    showCategories(categoryStates)

                    // Show recent games
                    val gameStates = recentGames.map { session ->
                        GameSessionState(
                            categoryName = session.category.name,
                            difficulty = session.difficulty.name,
                            score = session.totalScore,
                            starsEarned = session.starsEarned
                        )
                    }
                    showRecentGames(gameStates)
                    
                    Log.d(TAG, "All data displayed!")
                }
            },
            onError = { throwable ->
                Log.e(TAG, "Error loading home data: ${throwable.message}", throwable)
                executeIfViewAttached {
                    showError(throwable.message ?: "Failed to load home data")
                }
            },
            onStart = {
                Log.d(TAG, "onStart - showing loading...")
                executeIfViewAttached { showLoading() }
            },
            onFinally = {
                Log.d(TAG, "onFinally - hiding loading...")
                executeIfViewAttached { hideLoading() }
            }
        )
    }

    companion object {
        private const val TAG = "MainHomePresenter"
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

    private fun generateStreakDays(currentStreak: Int): List<StreakDayState> {
        val dayLabels = listOf("S", "M", "T", "W", "T", "F", "S")
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).dayOfWeek.value
        
        return dayLabels.mapIndexed { index, label ->
            // Simple logic: mark days as in streak based on current streak count
            val isInStreak = when {
                currentStreak == 0 -> false
                currentStreak >= 7 -> true
                else -> index < currentStreak
            }
            StreakDayState(label, isInStreak)
        }
    }

    fun onRefresh() {
        loadHomeData()
    }
}

