package com.bucharest.qurio.domain.entity

data class Character(
    val id: Int,
    val name: String,
    val age: String,
    val description: String,
    val price: Int,
    val isOwned: Boolean
)