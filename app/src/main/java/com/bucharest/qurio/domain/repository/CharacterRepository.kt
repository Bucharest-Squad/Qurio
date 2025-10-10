package com.bucharest.qurio.domain.repository

import com.bucharest.qurio.domain.entity.Character

interface CharacterRepository {
    suspend fun getAllCharacters(): List<Character>
    suspend fun getOwnedCharacters(): List<Character>
}