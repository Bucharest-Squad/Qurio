package com.bucharest.qurio.data.remote.dto

import com.bucharest.qurio.domain.entity.Question
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class QuestionDto(
    @SerializedName("category")
    val category: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("difficulty")
    val difficulty: String,
    @SerializedName("question")
    val question: String,
    @SerializedName("correct_answer")
    val correctAnswer: String,
    @SerializedName("incorrect_answers")
    val incorrectAnswers: List<String>
) {
    fun toEntity(): Question {
        return Question(
            id = UUID.randomUUID().toString(),
            category = category,
            type = type,
            difficulty = difficulty,
            question = question,
            correctAnswer = correctAnswer,
            incorrectAnswers = incorrectAnswers
        )
    }
}