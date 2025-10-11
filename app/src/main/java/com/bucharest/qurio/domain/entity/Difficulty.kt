package com.bucharest.qurio.domain.entity

enum class Difficulty(val value: String) {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard");

    companion object {
        fun fromString(value: String?): Difficulty =
            entries.firstOrNull { it.value == value } ?: EASY
    }
}