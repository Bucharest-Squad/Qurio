package com.bucharest.qurio.data.repository

import com.bucharest.qurio.data.local.dao.CharacterDao
import com.bucharest.qurio.data.local.dao.UserDao
import com.bucharest.qurio.data.local.mapper.toEntity
import com.bucharest.qurio.domain.entity.Character
import com.bucharest.qurio.domain.repository.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val characterDao: CharacterDao,
    private val userDao: UserDao
) : CharacterRepository {

    override suspend fun getAllCharacters(): List<Character> = 
        characterDao.getAll().map { it.toEntity() }

    override suspend fun getOwnedCharacters(): List<Character> = 
        characterDao.getAll().mapNotNull { it.takeIf { dto -> dto.owned }?.toEntity() }

    override suspend fun unlockCharacter(characterId: Int) {
        characterDao.updateOwnership(characterId,true)
        val user=userDao.getUser()
        val character=characterDao.getById(characterId)
        val updatedUser= user?.copy(coins = user.coins -character.price)
        if (updatedUser != null) {
            userDao.update(updatedUser)
        }
    }

    override suspend fun getCurrentCharacter(characterId: Int) : Character {
       return characterDao.getById(characterId).toEntity()
    }
}