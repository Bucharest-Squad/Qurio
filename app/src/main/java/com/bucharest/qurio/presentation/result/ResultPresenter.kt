package com.bucharest.qurio.presentation.result

import com.bucharest.qurio.domain.entity.GameSession
import com.bucharest.qurio.presentation.base.BasePresenter

class ResultPresenter : BasePresenter<ResultView>() {

    private var gameSession: GameSession? = null

    fun setGameSession(session: GameSession) {
        this.gameSession = session
        displayResult()
    }

    private fun displayResult() {
        val session = gameSession ?: return
        
        executeIfViewAttached {
            showGameResult(session)
            showStars(session.starsEarned)
            showCoinsEarned(session.coinsEarned)
            showCorrectAnswers(session.correctAnswers)
            showWrongAnswers(session.wrongAnswers)
            showSkippedAnswers(session.skippedAnswers)
            
            if (session.starsEarned > 0) {
                showWinState()
            } else {
                showLoseState()
            }
        }
    }

    fun onPlayAgainClicked() {
        val session = gameSession ?: return
        executeIfViewAttached {
            navigateToGame(
                categoryId = session.category.id,
                difficulty = session.difficulty.name,
                totalQuestions = session.totalQuestions
            )
        }
    }

    fun onHomeClicked() {
        executeIfViewAttached {
            navigateToHome()
        }
    }

    fun onShareClicked() {
        executeIfViewAttached {
            showShareDialog()
        }
    }
}
