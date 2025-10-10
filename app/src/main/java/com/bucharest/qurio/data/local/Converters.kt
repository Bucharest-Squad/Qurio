package com.bucharest.qurio.data.local

import androidx.room.TypeConverter
import com.bucharest.qurio.domain.entity.Achievement
import com.bucharest.qurio.domain.entity.Difficulty
import java.time.Instant

class Converters {

    @TypeConverter
    fun fromInstant(value: Instant?): Long? = value?.toEpochMilli()

    @TypeConverter
    fun toInstant(value: Long?): Instant? = value?.let { Instant.ofEpochMilli(it) }

    @TypeConverter
    fun fromDifficulty(value: Difficulty?): String? = value?.name

    @TypeConverter
    fun toDifficulty(value: String?): Difficulty? = value?.let { Difficulty.valueOf(it) }

    @TypeConverter
    fun fromAchievementCriteria(criteria: Achievement.Criteria): String {
        return when (criteria) {
            is Achievement.Criteria.CategoriesPlayedAtLeast -> "CATEGORIES:${criteria.count}"
            is Achievement.Criteria.CorrectAnswersAtLeast -> "CORRECT:${criteria.count}"
            is Achievement.Criteria.StarsEarnedAtLeast -> "STARS:${criteria.count}"
            is Achievement.Criteria.GamesPlayedAtLeast -> "GAMES:${criteria.count}"
            is Achievement.Criteria.OwnedCharactersAtLeast -> "OWNED:${criteria.count}"
            is Achievement.Criteria.CorrectStreakAtLeast -> "STREAK:${criteria.count}"
            is Achievement.Criteria.NoWrongAnswersInGame -> "PERFECT"
            is Achievement.Criteria.FastAnswerUnderSeconds -> "FAST:${criteria.seconds}"
            is Achievement.Criteria.ScoreAtLeast -> "SCORE:${criteria.score}"
        }
    }

    @TypeConverter
    fun toAchievementCriteria(value: String): Achievement.Criteria {
        if (value == PERFECT_GAME_KEY) return Achievement.Criteria.NoWrongAnswersInGame
        
        val parts = value.split(CRITERIA_SEPARATOR)
        val number = parts.getOrNull(VALUE_INDEX)?.toIntOrNull() ?: DEFAULT_CRITERIA_VALUE
        
        return when (parts[KEY_INDEX]) {
            CATEGORIES_KEY -> Achievement.Criteria.CategoriesPlayedAtLeast(number)
            CORRECT_KEY -> Achievement.Criteria.CorrectAnswersAtLeast(number)
            STARS_KEY -> Achievement.Criteria.StarsEarnedAtLeast(number)
            GAMES_KEY -> Achievement.Criteria.GamesPlayedAtLeast(number)
            OWNED_KEY -> Achievement.Criteria.OwnedCharactersAtLeast(number)
            STREAK_KEY -> Achievement.Criteria.CorrectStreakAtLeast(number)
            FAST_KEY -> Achievement.Criteria.FastAnswerUnderSeconds(number)
            SCORE_KEY -> Achievement.Criteria.ScoreAtLeast(number)
            else -> Achievement.Criteria.GamesPlayedAtLeast(DEFAULT_CRITERIA_VALUE)
        }
    }

    companion object {
        private const val CRITERIA_SEPARATOR = ":"
        private const val KEY_INDEX = 0
        private const val VALUE_INDEX = 1
        private const val DEFAULT_CRITERIA_VALUE = 0
        private const val PERFECT_GAME_KEY = "PERFECT"
        private const val CATEGORIES_KEY = "CATEGORIES"
        private const val CORRECT_KEY = "CORRECT"
        private const val STARS_KEY = "STARS"
        private const val GAMES_KEY = "GAMES"
        private const val OWNED_KEY = "OWNED"
        private const val STREAK_KEY = "STREAK"
        private const val FAST_KEY = "FAST"
        private const val SCORE_KEY = "SCORE"
    }
}