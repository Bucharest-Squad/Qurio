package com.bucharest.qurio.presentation.home.state

data class GameSessionUiModel(
    val categoryName: String,
    val difficulty: String,
    val score: Int,
    val starsEarned: Int,
    val coinsEarned: Int,
    val durationSeconds: Int,
    val playedDate: String
)