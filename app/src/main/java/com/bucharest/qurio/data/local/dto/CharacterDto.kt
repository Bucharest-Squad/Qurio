package com.bucharest.qurio.data.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character")
data class CharacterDto(
    @PrimaryKey 
    @ColumnInfo(name = "id") 
    val id: Int,
    
    @ColumnInfo(name = "name") 
    val name: String,
    
    @ColumnInfo(name = "age") 
    val age: String,
    
    @ColumnInfo(name = "description") 
    val description: String,
    
    @ColumnInfo(name = "price") 
    val price: Int,
    
    @ColumnInfo(name = "owned") 
    val owned: Boolean
)