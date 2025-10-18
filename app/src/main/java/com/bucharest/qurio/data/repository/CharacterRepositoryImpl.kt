package com.bucharest.qurio.data.repository

import com.bucharest.qurio.data.local.dao.CharacterDao
import com.bucharest.qurio.data.local.dao.UserDao
import com.bucharest.qurio.data.local.dto.CharacterDto
import com.bucharest.qurio.data.local.mapper.toEntity
import com.bucharest.qurio.domain.entity.Character
import com.bucharest.qurio.domain.repository.CharacterRepository
import com.bucharest.qurio.domain.repository.UserRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val characterDao: CharacterDao,
    private val userDao: UserDao,
    private val userRepository: UserRepository
) : CharacterRepository {

    override suspend fun getAllCharacters(): List<Character> = 
        characterDao.getAll().map { it.toEntity() }

    override suspend fun getOwnedCharacters(): List<Character> = 
        characterDao.getAll().mapNotNull { it.takeIf { dto -> dto.owned }?.toEntity() }

    override suspend fun unlockCharacter(characterId: Int) {
        val character = characterDao.getById(characterId)
        if (character != null) {
            // Use UserRepository's purchaseCharacter method for proper validation and error handling
            userRepository.purchaseCharacter(characterId, character.price)
            characterDao.updateOwnership(characterId, true)
        }
    }

    override suspend fun getCurrentCharacter(characterId: Int) : Character {
       return characterDao.getById(characterId).toEntity()
    }
}