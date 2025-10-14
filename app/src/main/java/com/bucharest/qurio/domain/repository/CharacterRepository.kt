package com.bucharest.qurio.domain.repository

import com.bucharest.qurio.domain.entity.Character

interface CharacterRepository {
    suspend fun getAllCharacters(): List<Character>
    suspend fun getOwnedCharacters(): List<Character>
    suspend fun unlockCharacter(characterId:Int)
    suspend fun getCurrentCharacter(characterId:Int) : Character
}