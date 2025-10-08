package com.bucharest.qurio.data.repository

import com.bucharest.qurio.data.remote.ApiService
import com.bucharest.qurio.domain.entity.Question
import com.bucharest.qurio.domain.repository.TriviaRepository
import javax.inject.Inject

class TriviaRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TriviaRepository {

    override suspend fun getQuestions(
        amount: Int,
        category: Int?,
        difficulty: String?,
        type: String?
    ): Result<List<Question>> {
        return try {
            val response = apiService.getQuestions(amount, category, difficulty, type)

            if (response.responseCode == 0) {
                Result.success(response.results.map { it.toEntity() })
            } else {
                Result.failure(Exception("Failed to fetch questions. Response code: ${response.responseCode}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}