package com.bucharest.qurio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bucharest.qurio.data.local.dto.AchievementDto

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievement")
    suspend fun getAll(): List<AchievementDto>

    @Query("SELECT * FROM achievement WHERE unlocked = 1")
    suspend fun getUnlocked(): List<AchievementDto>

    @Query("SELECT * FROM achievement WHERE id = :id")
    suspend fun getById(id: Int): AchievementDto?

    @Query("UPDATE achievement SET unlocked = :unlocked WHERE id = :id")
    suspend fun updateUnlocked(id: Int, unlocked: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<AchievementDto>)

    @Update
    suspend fun update(entity: AchievementDto)
}