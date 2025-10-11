package com.bucharest.qurio.data.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryDto(
    @PrimaryKey 
    @ColumnInfo(name = "id") 
    val id: Int,
    
    @ColumnInfo(name = "name") 
    val name: String
)