package com.bucharest.qurio.presentation.character_dialog

data class CharacterUiModel(
    val id: Int,
    val characterName: String,
    val characterDescription: String,
    val characterPrice: String,
    val characterAge: String,
    val imageRes: Pair<Int,Int>,
    val isOwned: Boolean,
    val canAfford: Boolean = false,
    val userCoins: Int = 0
)
