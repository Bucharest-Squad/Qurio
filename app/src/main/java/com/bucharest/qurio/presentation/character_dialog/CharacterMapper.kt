package com.bucharest.qurio.presentation.character_dialog

import com.bucharest.qurio.R
import com.bucharest.qurio.domain.entity.Character

object CharacterMapper {

    private const val CHARACTER_ID_RIKA = 1000
    private const val CHARACTER_ID_KAIYO = 1001
    private const val CHARACTER_ID_MIMI = 1002
    private const val CHARACTER_ID_YORU = 1003
    private const val CHARACTER_ID_KURO = 1004
    private const val CHARACTER_ID_MIKO = 1005
    private const val CHARACTER_ID_AORI = 1006
    private const val CHARACTER_ID_NARA = 1007
    private const val CHARACTER_ID_RENJI = 1008

    private fun getCharacterImages(characterId: Int): Pair<Int, Int> {
        return characterResourcesMap[characterId] ?: Pair(R.drawable.character_rika, R.drawable.rika_no_bg)
    }

    private fun getCharacterPrice(price: Int): String {
        return when {
            price < 1000 -> price.toString()
            price >= 1000 -> "${price / 1000}k"
            else -> "0"
        }
    }

    fun mapCharacterToUiState(character: Character): CharacterUiModel {
        val uiResources = getCharacterImages(character.id)
        return CharacterUiModel(
            id = character.id,
            imageRes = uiResources,
            characterName = character.name,
            characterDescription = character.description,
            characterPrice = getCharacterPrice(character.price),
            characterAge = character.age,
            isOwned = character.isOwned,
        )
    }
    fun getCharacterName(character: Character): CharacterUiModel {
        val uiResources = getCharacterImages(character.id)
        return CharacterUiModel(
            id = character.id,
            imageRes = uiResources,
            characterName = character.name,
            characterDescription = character.description,
            characterPrice = getCharacterPrice(character.price),
            characterAge = character.age,
            isOwned = character.isOwned,
        )
    }

    private val characterResourcesMap = mapOf(
        CHARACTER_ID_RIKA to Pair(R.drawable.character_rika, R.drawable.rika_no_bg),
        CHARACTER_ID_KAIYO to Pair(R.drawable.character_kaiyo, R.drawable.kaiyo_no_bg),
        CHARACTER_ID_MIMI to Pair(R.drawable.character_mimi, R.drawable.mimi_no_bg),
        CHARACTER_ID_YORU to Pair(R.drawable.character_youru, R.drawable.yoru_no_bg),
        CHARACTER_ID_KURO to Pair(R.drawable.character_kuro, R.drawable.kuro_no_bg),
        CHARACTER_ID_MIKO to Pair(R.drawable.character_miko, R.drawable.miko_no_bg),
        CHARACTER_ID_AORI to Pair(R.drawable.character_aori, R.drawable.aori_no_bg),
        CHARACTER_ID_NARA to Pair(R.drawable.character_nara, R.drawable.nara_no_bg),
        CHARACTER_ID_RENJI to Pair(R.drawable.character_renji, R.drawable.renji_no_bg)
    )
}
