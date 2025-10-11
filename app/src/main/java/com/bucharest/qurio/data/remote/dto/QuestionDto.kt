package com.bucharest.qurio.data.remote.dto

import android.text.Html
import com.bucharest.qurio.domain.entity.Category
import com.bucharest.qurio.domain.entity.Difficulty
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
    private fun String.fromHtml(): String =
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    
    fun toEntity(categories: List<Category>): Question {
        val categoryEntity = categories.firstOrNull { it.name.equals(category, ignoreCase = true) }
            ?: categories.first { it.id == 9 }

        val answers = incorrectAnswers.map { Question.Answer(it.fromHtml(), false) } +
                Question.Answer(correctAnswer.fromHtml(), true)

        return Question(
            id = UUID.randomUUID().toString(),
            category = categoryEntity,
            type = Question.Type.fromString(type),
            difficulty = Difficulty.fromString(difficulty),
            question = question.fromHtml(),
            answers = answers.shuffled()
        )
    }
}