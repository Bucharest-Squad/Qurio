package com.bucharest.qurio.data.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserDto(
    @PrimaryKey 
    @ColumnInfo(name = "id") 
    val id: Int,
    
    @ColumnInfo(name = "current_character_id") 
    val currentCharacterId: Int,
    
    @ColumnInfo(name = "coins") 
    val coins: Int,
    
    @ColumnInfo(name = "lives") 
    val lives: Int,
    
    @ColumnInfo(name = "music_volume") 
    val musicVolume: Float,
    
    @ColumnInfo(name = "effects_volume") 
    val effectsVolume: Float,
    
    @ColumnInfo(name = "last_played_epoch_day") 
    val lastPlayedEpochDay: Long?,
    
    @ColumnInfo(name = "current_daily_streak") 
    val currentDailyStreak: Int,
    
    @ColumnInfo(name = "streak_start_epoch_day") 
    val streakStartEpochDay: Long?
)