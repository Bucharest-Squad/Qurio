package com.bucharest.qurio.data.repository

import com.bucharest.qurio.data.remote.ApiService
import com.bucharest.qurio.domain.entity.Question
import com.bucharest.qurio.domain.exception.InvalidRequestException
import com.bucharest.qurio.domain.exception.NetworkErrorException
import com.bucharest.qurio.domain.exception.NoQuestionsAvailableException
import com.bucharest.qurio.domain.model.QuestionFilter
import com.bucharest.qurio.domain.repository.TriviaRepository
import javax.inject.Inject

class TriviaRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val categoryRepository: CategoryRepositoryImpl
) : TriviaRepository {

    override suspend fun getQuestions(filter: QuestionFilter): List<Question> = runCatching {
        val categories = categoryRepository.getAllCategories()
        
        apiService.getQuestions(
            amount = filter.amount,
            categoryId = filter.categoryId,
            difficulty = filter.difficulty,
            type = filter.type
        ).let { response ->
            when (response.responseCode) {
                RESPONSE_SUCCESS -> response.results.map { it.toEntity(categories) }
                RESPONSE_NO_RESULTS -> throw NoQuestionsAvailableException()
                RESPONSE_INVALID_PARAMETER -> throw InvalidRequestException("Invalid parameters")
                RESPONSE_TOKEN_NOT_FOUND -> throw InvalidRequestException("Token not found")
                else -> throw InvalidRequestException("Unknown error: ${response.responseCode}")
            }
        }
    }.getOrElse { throw NetworkErrorException(it.localizedMessage) }

    private companion object {
        const val RESPONSE_SUCCESS = 0
        const val RESPONSE_NO_RESULTS = 1
        const val RESPONSE_INVALID_PARAMETER = 2
        const val RESPONSE_TOKEN_NOT_FOUND = 3
    }
}