package com.bucharest.qurio.data.repository

import com.bucharest.qurio.data.local.dao.GameSessionDao
import com.bucharest.qurio.data.mapper.toDto
import com.bucharest.qurio.data.mapper.toEntity
import com.bucharest.qurio.data.remote.ApiService
import com.bucharest.qurio.data.local.AchievementManager
import com.bucharest.qurio.domain.entity.Category
import com.bucharest.qurio.domain.entity.Difficulty
import com.bucharest.qurio.domain.entity.GameSession
import com.bucharest.qurio.domain.entity.Question
import com.bucharest.qurio.domain.model.AnswerSubmission
import com.bucharest.qurio.domain.model.GameConfig
import com.bucharest.qurio.domain.model.QuestionFilter
import com.bucharest.qurio.domain.repository.GameRepository
import com.bucharest.qurio.domain.repository.TriviaRepository
import java.time.Instant
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val triviaRepository: TriviaRepository,
    private val gameSessionDao: GameSessionDao,
    private val achievementManager: AchievementManager,
    private val userRepository: UserRepositoryImpl,
    private val categoryRepository: CategoryRepositoryImpl
) : GameRepository {

    override suspend fun startGame(category: Category, difficulty: Difficulty, totalQuestions: Int): GameSession =
        GameSession(
            id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
            category = category,
            difficulty = difficulty,
            startedAt = Instant.now(),
            finishedAt = null,
            totalQuestions = totalQuestions,
            correctAnswers = 0,
            wrongAnswers = 0,
            skippedAnswers = 0,
            starsEarned = 0,
            coinsEarned = 0,
            livesLost = 0,
            totalScore = 0,
            fastestAnswerSeconds = null
        ).also { gameSessionDao.insert(it.toDto()) }

    override suspend fun fetchQuestions(filter: QuestionFilter): List<Question> =
        triviaRepository.getQuestions(filter)

    override suspend fun submitAnswer(submission: AnswerSubmission): GameSession =
        updateSession(submission.sessionId) { session ->
            val isWrong = !submission.isCorrect && !submission.isSkipped
            
            session.copy(
                correctAnswers = session.correctAnswers + submission.isCorrect.toInt(),
                wrongAnswers = session.wrongAnswers + isWrong.toInt(),
                skippedAnswers = session.skippedAnswers + submission.isSkipped.toInt(),
                starsEarned = session.starsEarned + if (submission.isCorrect) submission.starsForCorrect else 0,
                livesLost = session.livesLost + if (isWrong) submission.livesLostForWrong else 0,
                totalScore = session.totalScore + when {
                    submission.isCorrect -> POINTS_PER_CORRECT
                    !submission.isSkipped -> POINTS_PER_WRONG
                    else -> 0
                },
                fastestAnswerSeconds = session.fastestAnswerSeconds.updateFastest(
                    submission.answerTimeSeconds.takeIf { submission.isCorrect && !submission.isSkipped }
                )
            )
        }

    override suspend fun finishGame(sessionId: Int, config: GameConfig): GameSession =
        updateSession(sessionId) { session ->
            session.copy(
                finishedAt = Instant.now(),
                coinsEarned = session.correctAnswers * config.coinsPerCorrect + 
                             session.skippedAnswers * config.coinsPerSkipped,
                starsEarned = calculateStars(session)
            )
        }.also {
            userRepository.updateStreakAfterGamePlayed()
        }

    override suspend fun getRecentSessions(limit: Int): List<GameSession> =
        gameSessionDao.getRecent(limit).map { dto ->
            val category = categoryRepository.getCategoryById(dto.categoryId)
                ?: throw IllegalStateException("Category not found")
            dto.toEntity(category)
        }

    private suspend inline fun updateSession(
        sessionId: Int, 
        transform: (GameSession) -> GameSession
    ): GameSession {
        val sessionDto = gameSessionDao.getById(sessionId)
            ?: throw IllegalStateException("Session not found")
        
        val category = categoryRepository.getCategoryById(sessionDto.categoryId)
            ?: throw IllegalStateException("Category not found")
        
        val session = sessionDto.toEntity(category)
        
        return transform(session).also { updated ->
            gameSessionDao.update(updated.toDto())
            achievementManager.evaluateAndUnlockAchievements()
        }
    }

    private fun calculateStars(session: GameSession): Int {
        if (session.totalQuestions == 0) return 0
        
        val accuracy = session.correctAnswers.toDouble() / session.totalQuestions
        return when {
            session.correctAnswers == session.totalQuestions -> STARS_PERFECT
            accuracy >= ACCURACY_TWO_STARS && session.skippedAnswers <= MAX_SKIPS_FOR_TWO_STARS -> STARS_GOOD
            accuracy >= ACCURACY_ONE_STAR -> STARS_OK
            else -> 0
        }
    }

    private fun Boolean.toInt() = if (this) 1 else 0
    
    private fun Int?.updateFastest(newTime: Int?): Int? = when {
        newTime == null -> this
        this == null -> newTime
        else -> minOf(this, newTime)
    }

    companion object {
        private const val POINTS_PER_CORRECT = 50
        private const val POINTS_PER_WRONG = -50
        private const val STARS_PERFECT = 3
        private const val STARS_GOOD = 2
        private const val STARS_OK = 1
        private const val ACCURACY_TWO_STARS = 0.8
        private const val ACCURACY_ONE_STAR = 0.5
        private const val MAX_SKIPS_FOR_TWO_STARS = 1
    }
}