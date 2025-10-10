package com.bucharest.qurio.domain.repository

import com.bucharest.qurio.domain.entity.Category
import com.bucharest.qurio.domain.entity.Difficulty
import com.bucharest.qurio.domain.entity.GameSession
import com.bucharest.qurio.domain.entity.Question
import com.bucharest.qurio.domain.model.AnswerSubmission
import com.bucharest.qurio.domain.model.GameConfig
import com.bucharest.qurio.domain.model.QuestionFilter

interface GameRepository {
    suspend fun startGame(category: Category, difficulty: Difficulty, totalQuestions: Int): GameSession
    suspend fun fetchQuestions(filter: QuestionFilter): List<Question>
    suspend fun submitAnswer(submission: AnswerSubmission): GameSession
    suspend fun finishGame(sessionId: Int, config: GameConfig = GameConfig.default()): GameSession
    suspend fun getRecentSessions(limit: Int): List<GameSession>
}