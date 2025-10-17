package com.bucharest.qurio.presentation.game

import com.bucharest.qurio.domain.entity.Question
import com.bucharest.qurio.presentation.base.BaseView

interface GameView : BaseView {
    fun showQuestion(question: Question, questionNumber: String)
    fun showAnswers(answers: List<String>)
    fun updateTimer(secondsLeft: Long, progress: Float)
    fun showScoreIndicator(isCorrect: Boolean)
    fun highlightAnswers(correctAnswer: String, selectedPosition: Int)
    fun resetAnswers()
    fun updateLivesCount(lives: Int)
    fun updateScore(score: Int)
    fun showGameEnd(session: com.bucharest.qurio.domain.entity.GameSession)
    fun showTimeUp()
    fun showNoLivesLeft()
    fun navigateBack()
    fun navigateToResult(session: com.bucharest.qurio.domain.entity.GameSession)
}


