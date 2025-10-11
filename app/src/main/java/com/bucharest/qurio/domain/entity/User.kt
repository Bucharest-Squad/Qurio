package com.bucharest.qurio.domain.entity

import kotlinx.datetime.LocalDate

data class User(
    val id: Int,
    val currentCharacterId: Int,
    val coins: Int,
    val lives: Int,
    val lastPlayedDate: LocalDate?,
    val currentDailyStreak: Int,
    val streakStartDate: LocalDate?,
    val musicVolume: Float,
    val effectsVolume: Float
)