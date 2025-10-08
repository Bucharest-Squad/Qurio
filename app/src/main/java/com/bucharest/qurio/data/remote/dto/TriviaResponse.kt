package com.bucharest.qurio.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TriviaResponse(
    @SerializedName("response_code")
    val responseCode: Int,
    @SerializedName("results")
    val results: List<QuestionDto>
)