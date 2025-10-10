package com.bucharest.qurio.data.repository

import com.bucharest.qurio.data.local.dao.AchievementDao
import com.bucharest.qurio.data.local.mapper.toEntity
import com.bucharest.qurio.domain.entity.Achievement
import com.bucharest.qurio.domain.repository.AchievementRepository
import javax.inject.Inject

class AchievementRepositoryImpl @Inject constructor(
    private val achievementDao: AchievementDao
) : AchievementRepository {

    override suspend fun getAllAchievements(): List<Achievement> {
        return achievementDao.getAll().map { it.toEntity() }
    }

    override suspend fun getUnlockedAchievements(): List<Achievement> {
        return achievementDao.getUnlocked().map { it.toEntity() }
    }
}