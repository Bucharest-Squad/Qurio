package com.bucharest.qurio.domain.model

data class QuestionFilter(
    val amount: Int = 10,
    val categoryId: Int? = null,
    val difficulty: String? = null,
    val type: String? = null
)