package com.bucharest.qurio.data.repository

import com.bucharest.qurio.data.local.dao.CharacterDao
import com.bucharest.qurio.data.local.mapper.toEntity
import com.bucharest.qurio.domain.entity.Character
import com.bucharest.qurio.domain.repository.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val characterDao: CharacterDao
) : CharacterRepository {

    override suspend fun getAllCharacters(): List<Character> = 
        characterDao.getAll().map { it.toEntity() }

    override suspend fun getOwnedCharacters(): List<Character> = 
        characterDao.getAll().mapNotNull { it.takeIf { dto -> dto.owned }?.toEntity() }
}