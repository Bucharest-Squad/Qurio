package com.bucharest.qurio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bucharest.qurio.data.local.dto.CategoryDto

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category ORDER BY name ASC")
    suspend fun getAll(): List<CategoryDto>

    @Query("SELECT * FROM category WHERE id = :id")
    suspend fun getById(id: Int): CategoryDto?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<CategoryDto>)
}