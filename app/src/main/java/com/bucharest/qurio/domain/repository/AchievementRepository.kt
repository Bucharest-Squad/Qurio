package com.bucharest.qurio.domain.repository

import com.bucharest.qurio.domain.entity.Achievement

interface AchievementRepository {
    suspend fun getAllAchievements(): List<Achievement>
    suspend fun getUnlockedAchievements(): List<Achievement>
}