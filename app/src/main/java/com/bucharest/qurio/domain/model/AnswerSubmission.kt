package com.bucharest.qurio.domain.model

data class AnswerSubmission(
    val sessionId: Int,
    val questionId: String,
    val isCorrect: Boolean,
    val isSkipped: Boolean,
    val answerTimeSeconds: Int,
    val starsForCorrect: Int,
    val livesLostForWrong: Int
)