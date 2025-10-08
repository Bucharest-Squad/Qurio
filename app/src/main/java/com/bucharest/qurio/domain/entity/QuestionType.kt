package com.bucharest.qurio.domain.entity

enum class QuestionType(val value: String) {
    MULTIPLE("multiple"),
    BOOLEAN("boolean");

    companion object {
        fun fromString(value: String?): QuestionType =
            entries.firstOrNull { it.value == value } ?: MULTIPLE
    }
}