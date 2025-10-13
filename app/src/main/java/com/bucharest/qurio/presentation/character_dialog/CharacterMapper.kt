package com.bucharest.qurio.presentation.character_dialog

import android.content.Context
import androidx.core.content.ContextCompat
import com.bucharest.qurio.R
import com.bucharest.qurio.domain.entity.Category
import com.bucharest.qurio.domain.entity.Character
import com.bucharest.qurio.presentation.home.state.CategoryUiModel

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


    private fun getCharacterImage(characterId: Int): Int {
        return characterResourcesMap[characterId] ?: R.drawable.character_rika
    }
    private fun getCharacterPrice(price: Int): String {
        return when {
            price < 1000 -> price.toString()
            price > 1000 -> {
                val parsedPrice = price.toString().substring(0,2)+"k"
                parsedPrice
            }
            else -> "0"
        }

    }
     fun mapCharacterToUiState(character: Character): CharacterUiModel {
        val uiResources =getCharacterImage(character.id)
        return CharacterUiModel(
            id = character.id,
            imageRes = uiResources,
            characterName =character.name,
            characterDescription = character.description,
            characterPrice = getCharacterPrice(character.price),
            characterAge = character.age,
            isOwned = character.isOwned,
        )
    }
    private val characterResourcesMap = mapOf(
        CHARACTER_ID_RIKA to R.drawable.character_rika,
        CHARACTER_ID_KAIYO to R.drawable.character_kaiyo,
        CHARACTER_ID_MIMI to R.drawable.character_mimi,
        CHARACTER_ID_YORU to R.drawable.character_youru,
        CHARACTER_ID_KURO to R.drawable.character_kuro,
        CHARACTER_ID_MIKO to R.drawable.character_miko,
        CHARACTER_ID_AORI to R.drawable.character_aori,
        CHARACTER_ID_NARA to R.drawable.character_nara,
        CHARACTER_ID_RENJI to R.drawable.character_renji
    )


}