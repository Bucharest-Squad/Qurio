package com.bucharest.qurio.presentation.result

import com.bucharest.qurio.audio.AudioManager
import com.bucharest.qurio.domain.entity.GameSession
import com.bucharest.qurio.domain.repository.UserRepository
import com.bucharest.qurio.presentation.base.BasePresenter

class ResultPresenter(
    private val audioManager: AudioManager,
    private val userRepository: UserRepository
) : BasePresenter<ResultView>() {

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
                audioManager.playGameWin()
                showWinState()
            } else {
                audioManager.playGameOver()
                showLoseState()
            }
            
            // Check if user has lives to play again
            checkUserLives()
        }
    }
    
    private fun checkUserLives() {
        tryToExecute(
            execute = { userRepository.getUser().lives },
            onSuccess = { lives ->
                executeIfViewAttached {
                    if (lives <= 0) {
                        hidePlayAgainButton()
                    }
                }
            },
            onError = { 
                // If we can't get user data, hide the button to be safe
                executeIfViewAttached {
                    hidePlayAgainButton()
                }
            }
        )
    }

    fun onPlayAgainClicked() {
        audioManager.playButtonPress()
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
        audioManager.playButtonPress()
        executeIfViewAttached {
            navigateToHome()
        }
    }

    fun onShareClicked() {
        audioManager.playButtonPress()
        executeIfViewAttached {
            showShareDialog()
        }
    }
}
