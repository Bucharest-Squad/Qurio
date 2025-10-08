package com.bucharest.qurio.data.remote

import com.bucharest.qurio.data.remote.dto.TriviaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int,
        @Query("category") category: Int? = null,
        @Query("difficulty") difficulty: String? = null,
        @Query("type") type: String? = null
    ): TriviaResponse
}