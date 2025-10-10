package com.bucharest.qurio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bucharest.qurio.data.local.dto.CharacterDto

@Dao
interface CharacterDao {
    @Query("SELECT * FROM `character`")
    suspend fun getAll(): List<CharacterDto>

    @Query("SELECT * FROM `character` WHERE id = :id")
    suspend fun getById(id: Int): CharacterDto?

    @Query("UPDATE `character` SET owned = :owned WHERE id = :id")
    suspend fun updateOwnership(id: Int, owned: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<CharacterDto>)
}