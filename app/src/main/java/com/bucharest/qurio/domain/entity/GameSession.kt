package com.bucharest.qurio.domain.entity

import java.time.Instant

data class GameSession(
    val id: Int,
    val category: Category,
    val difficulty: Difficulty,
    val startedAt: Instant,
    val finishedAt: Instant?,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val skippedAnswers: Int,
    val starsEarned: Int,
    val coinsEarned: Int,
    val livesLost: Int,
    val totalScore: Int,
    val fastestAnswerSeconds: Int?
)