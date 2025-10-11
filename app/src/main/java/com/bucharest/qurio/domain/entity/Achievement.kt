package com.bucharest.qurio.domain.entity

data class Achievement(
    val id: Int,
    val name: String,
    val description: String,
    val criteria: Criteria,
    val isUnlocked: Boolean
) {
    sealed class Criteria {
        data class CategoriesPlayedAtLeast(val count: Int) : Criteria()
        data class CorrectAnswersAtLeast(val count: Int) : Criteria()
        data class StarsEarnedAtLeast(val count: Int) : Criteria()
        data class GamesPlayedAtLeast(val count: Int) : Criteria()
        data class OwnedCharactersAtLeast(val count: Int) : Criteria()
        data class CorrectStreakAtLeast(val count: Int) : Criteria()
        data object NoWrongAnswersInGame : Criteria()
        data class FastAnswerUnderSeconds(val seconds: Int) : Criteria()
        data class ScoreAtLeast(val score: Int) : Criteria()
    }
}