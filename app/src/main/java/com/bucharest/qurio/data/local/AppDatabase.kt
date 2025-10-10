package com.bucharest.qurio.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bucharest.qurio.data.local.dao.AchievementDao
import com.bucharest.qurio.data.local.dao.CharacterDao
import com.bucharest.qurio.data.local.dao.GameSessionDao
import com.bucharest.qurio.data.local.dao.CategoryDao
import com.bucharest.qurio.data.local.dao.UserDao
import com.bucharest.qurio.data.local.dto.AchievementDto
import com.bucharest.qurio.data.local.dto.CharacterDto
import com.bucharest.qurio.data.local.dto.GameSessionDto
import com.bucharest.qurio.data.local.dto.CategoryDto
import com.bucharest.qurio.data.local.dto.UserDto

@Database(
    entities = [
        UserDto::class,
        CharacterDto::class,
        AchievementDto::class,
        GameSessionDto::class,
        CategoryDto::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun characterDao(): CharacterDao
    abstract fun achievementDao(): AchievementDao
    abstract fun gameSessionDao(): GameSessionDao
    abstract fun categoryDao(): CategoryDao
}