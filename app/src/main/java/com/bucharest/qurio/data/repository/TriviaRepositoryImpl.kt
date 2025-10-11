package com.bucharest.qurio.data.repository

import com.bucharest.qurio.data.remote.ApiService
import com.bucharest.qurio.data.remote.dto.TriviaResponse
import com.bucharest.qurio.domain.entity.Question
import com.bucharest.qurio.domain.exception.InvalidRequestException
import com.bucharest.qurio.domain.exception.NetworkErrorException
import com.bucharest.qurio.domain.exception.NoQuestionsAvailableException
import com.bucharest.qurio.domain.repository.TriviaRepository
import javax.inject.Inject

class TriviaRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TriviaRepository {

    override suspend fun getQuestions(
        amount: Int,
        categoryId: Int?,
        difficulty: String?,
        type: String?
    ): List<Question> {
        return safeApiCall { apiService.getQuestions(amount, categoryId, difficulty, type) }
    }

    private suspend fun safeApiCall(apiCall: suspend () -> TriviaResponse): List<Question> {
        return try {
            val response = apiCall()
            handleResponse(response)
        } catch (e: Exception) {
            throw NetworkErrorException(e.localizedMessage)
        }
    }

    private fun handleResponse(response: TriviaResponse): List<Question> {
        return when (response.responseCode) {
            0 -> response.results.map { it.toEntity() }
            1 -> throw NoQuestionsAvailableException()
            2 -> throw InvalidRequestException("Invalid parameters")
            3 -> throw InvalidRequestException("Token not found")
            else -> throw InvalidRequestException("Unknown error: ${response.responseCode}")
        }
    }
}