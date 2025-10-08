package com.bucharest.qurio.domain.repository

import com.bucharest.qurio.domain.entity.Question

interface TriviaRepository {
    suspend fun getQuestions(
        amount: Int = 10,
        category: Int? = null,
        difficulty: String? = null,
        type: String? = null
    ): Result<List<Question>>
}