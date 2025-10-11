package com.bucharest.qurio.domain.entity

data class Question(
    val id: String,
    val category: Category,
    val type: QuestionType,
    val difficulty: Difficulty,
    val question: String,
    val answers: List<Answer>
) {
    data class Answer(
        val text: String,
        val isCorrect: Boolean
    )
}