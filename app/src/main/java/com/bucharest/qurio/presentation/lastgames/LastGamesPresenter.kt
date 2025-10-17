package com.bucharest.qurio.presentation.lastgames

import android.content.Context
import com.bucharest.qurio.domain.entity.GameSession
import com.bucharest.qurio.domain.repository.GameRepository
import com.bucharest.qurio.presentation.base.BasePresenter
import com.bucharest.qurio.presentation.home.state.GameSessionUiModel
import com.bucharest.qurio.presentation.utils.NetworkUtils
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.milliseconds

class LastGamesPresenter(
    private val gameRepository: GameRepository,
    private val context: Context
) : BasePresenter<LastGamesView>() {

    override fun onViewAttached() {
        super.onViewAttached()
        loadGames()
    }

    fun loadGames() {
        if (!NetworkUtils.isConnectedToInternet(context)) {
            executeIfViewAttached { showError("No internet connection") }
            return
        }
        
        tryToExecute(
            execute = { gameRepository.getAllSessions() },
            onSuccess = ::handleGamesSuccess,
            onError = ::handleGamesError,
            onStart = { executeIfViewAttached { showLoading() } },
            onFinally = { executeIfViewAttached { hideLoading() } }
        )
    }

    fun onGameClicked(game: GameSessionUiModel) {
        executeIfViewAttached {
            showMessage("Game: ${game.categoryName} - ${game.score} pts")
        }
    }

    private fun handleGamesSuccess(sessions: List<GameSession>) {
        val gameStates = mapGameSessionsToStates(sessions)
        executeIfViewAttached {
            showGames(gameStates)
        }
    }

    private fun handleGamesError(throwable: Throwable) {
        executeIfViewAttached {
            showError(throwable.message ?: "Failed to load games")
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
            0
        }
    }

    private fun formatPlayedDate(session: GameSession): String {
        val instant = Instant.fromEpochMilliseconds(session.startedAt.toEpochMilli())
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${localDateTime.dayOfMonth}/${localDateTime.monthNumber}/${localDateTime.year}"
    }
}
