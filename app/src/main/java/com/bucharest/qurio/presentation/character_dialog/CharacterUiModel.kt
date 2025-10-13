package com.bucharest.qurio.presentation.character_dialog

data class CharacterUiModel(
    val id: Int,
    val characterName: String,
    val characterDescription: String,
    val characterPrice: String,
    val characterAge: String,
    val imageRes: Int,
    val isOwned: Boolean,
)
