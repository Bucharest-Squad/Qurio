package com.bucharest.qurio.data.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bucharest.qurio.domain.entity.Achievement

@Entity(tableName = "achievement")
data class AchievementDto(
    @PrimaryKey 
    @ColumnInfo(name = "id") 
    val id: Int,
    
    @ColumnInfo(name = "name") 
    val name: String,
    
    @ColumnInfo(name = "description") 
    val description: String,
    
    @ColumnInfo(name = "criteria") 
    val criteria: Achievement.Criteria,
    
    @ColumnInfo(name = "unlocked") 
    val unlocked: Boolean
)