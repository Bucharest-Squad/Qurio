package com.bucharest.qurio.presentation.result

import com.bucharest.qurio.domain.entity.GameSession
import com.bucharest.qurio.presentation.base.BaseView

interface ResultView : BaseView {
    fun showGameResult(session: GameSession)
    fun showStars(count: Int)
    fun showCoinsEarned(coins: Int)
    fun showCorrectAnswers(count: Int)
    fun showWrongAnswers(count: Int)
    fun showSkippedAnswers(count: Int)
    fun showWinState()
    fun showLoseState()
    fun showShareDialog()
    fun hidePlayAgainButton()
    fun navigateToHome()
    fun navigateToGame(categoryId: Int, difficulty: String, totalQuestions: Int)
}
