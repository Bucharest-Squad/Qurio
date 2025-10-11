package com.bucharest.qurio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bucharest.qurio.data.local.dto.UserDto

@Dao
interface UserDao {
    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getUser(): UserDto?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserDto)

    @Update
    suspend fun update(user: UserDto)
}