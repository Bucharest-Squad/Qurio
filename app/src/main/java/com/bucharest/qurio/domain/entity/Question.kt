package com.bucharest.qurio.domain.entity

data class Question(
    val id: String,
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>,
    val allAnswers: List<String> = (incorrectAnswers + correctAnswer).shuffled()
)