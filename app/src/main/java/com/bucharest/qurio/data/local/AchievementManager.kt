package com.bucharest.qurio.data.local

import com.bucharest.qurio.data.local.dao.AchievementDao
import com.bucharest.qurio.data.local.dao.CharacterDao
import com.bucharest.qurio.data.local.dao.GameSessionDao
import com.bucharest.qurio.data.local.dao.UserDao
import com.bucharest.qurio.data.mapper.toEntity
import com.bucharest.qurio.domain.entity.Achievement
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementManager @Inject constructor(
    private val achievementDao: AchievementDao,
    private val gameSessionDao: GameSessionDao,
    private val userDao: UserDao,
    private val characterDao: CharacterDao
) {

    suspend fun evaluateAndUnlockAchievements() {
        val lockedAchievements = getLockedAchievements()
        if (lockedAchievements.isEmpty()) return
        
        val statsData = gatherGameStatistics()
        evaluateAndUnlockEachAchievement(lockedAchievements, statsData)
    }

    private suspend fun getLockedAchievements() =
        achievementDao.getAll().filterNot { it.unlocked }

    private suspend fun gatherGameStatistics(): AchievementEvaluationData {
        val latestSession = gameSessionDao.getMostRecent()
        
        return AchievementEvaluationData(
            totalGamesPlayed = gameSessionDao.countFinished(),
            totalCorrectAnswers = gameSessionDao.sumCorrectAnswers(),
            totalStars = gameSessionDao.sumStarsEarned(),
            distinctCategoriesPlayed = gameSessionDao.countDistinctCategoriesPlayed(),
            ownedCharactersCount = characterDao.getAll().count { it.owned },
            currentStreak = userDao.getUser()?.currentDailyStreak ?: DEFAULT_STREAK,
            latestGameScore = latestSession?.totalScore ?: DEFAULT_SCORE,
            latestGameHadNoWrongAnswers = isFlawlessGame(latestSession),
            fastestAnswerTimeEver = gameSessionDao.getFastestAnswerTimeEver()
        )
    }

    private fun isFlawlessGame(session: com.bucharest.qurio.data.local.dto.GameSessionDto?): Boolean {
        return session?.let { 
            it.wrongAnswers == NO_WRONG_ANSWERS && it.finishedAt != null 
        } ?: false
    }

    private suspend fun evaluateAndUnlockEachAchievement(
        achievements: List<com.bucharest.qurio.data.local.dto.AchievementDto>,
        statsData: AchievementEvaluationData
    ) {
        achievements.forEach { achievement ->
            if (shouldUnlockAchievement(achievement, statsData)) {
                achievementDao.updateUnlocked(achievement.id, unlocked = true)
            }
        }
    }

    private fun shouldUnlockAchievement(
        achievement: com.bucharest.qurio.data.local.dto.AchievementDto,
        stats: AchievementEvaluationData
    ): Boolean {
        return evaluateCriteria(
            criteria = achievement.toEntity().criteria,
            totalGamesPlayed = stats.totalGamesPlayed,
            totalCorrectAnswers = stats.totalCorrectAnswers,
            totalStars = stats.totalStars,
            distinctCategoriesPlayed = stats.distinctCategoriesPlayed,
            ownedCharactersCount = stats.ownedCharactersCount,
            currentStreak = stats.currentStreak,
            latestGameScore = stats.latestGameScore,
            latestGameHadNoWrongAnswers = stats.latestGameHadNoWrongAnswers,
            fastestAnswerTimeEver = stats.fastestAnswerTimeEver
        )
    }

    private data class AchievementEvaluationData(
        val totalGamesPlayed: Int,
        val totalCorrectAnswers: Int,
        val totalStars: Int,
        val distinctCategoriesPlayed: Int,
        val ownedCharactersCount: Int,
        val currentStreak: Int,
        val latestGameScore: Int,
        val latestGameHadNoWrongAnswers: Boolean,
        val fastestAnswerTimeEver: Int?
    )

    private fun evaluateCriteria(
        criteria: Achievement.Criteria,
        totalGamesPlayed: Int,
        totalCorrectAnswers: Int,
        totalStars: Int,
        distinctCategoriesPlayed: Int,
        ownedCharactersCount: Int,
        currentStreak: Int,
        latestGameScore: Int,
        latestGameHadNoWrongAnswers: Boolean,
        fastestAnswerTimeEver: Int?
    ): Boolean {
        return when (criteria) {
            is Achievement.Criteria.GamesPlayedAtLeast -> 
                totalGamesPlayed >= criteria.count
            is Achievement.Criteria.CorrectAnswersAtLeast -> 
                totalCorrectAnswers >= criteria.count
            is Achievement.Criteria.StarsEarnedAtLeast -> 
                totalStars >= criteria.count
            is Achievement.Criteria.CategoriesPlayedAtLeast -> 
                distinctCategoriesPlayed >= criteria.count
            is Achievement.Criteria.OwnedCharactersAtLeast -> 
                ownedCharactersCount >= criteria.count
            is Achievement.Criteria.CorrectStreakAtLeast -> 
                currentStreak >= criteria.count
            is Achievement.Criteria.NoWrongAnswersInGame -> 
                latestGameHadNoWrongAnswers
            is Achievement.Criteria.ScoreAtLeast -> 
                latestGameScore >= criteria.score
            is Achievement.Criteria.FastAnswerUnderSeconds -> 
                fastestAnswerTimeEver != null && fastestAnswerTimeEver <= criteria.seconds
        }
    }

    companion object {
        private const val DEFAULT_STREAK = 0
        private const val DEFAULT_SCORE = 0
        private const val NO_WRONG_ANSWERS = 0
    }
}