package com.bucharest.qurio.domain.entity

data class Question(
    val id: String,
    val category: Category,
    val type: Type,
    val difficulty: Difficulty,
    val question: String,
    val answers: List<Answer>
) {
    data class Answer(
        val text: String,
        val isCorrect: Boolean
    )

    enum class Type(val value: String) {
        MULTIPLE("multiple"),
        BOOLEAN("boolean");

        companion object {
            fun fromString(value: String?): Type =
                entries.firstOrNull { it.value == value } ?: MULTIPLE
        }
    }
}