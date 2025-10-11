package com.bucharest.qurio.domain.repository

import com.bucharest.qurio.domain.entity.User

interface UserRepository {
    suspend fun getUser(): User
    suspend fun updateCoins(delta: Int): User
    suspend fun updateLives(delta: Int): User
    suspend fun setActiveCharacter(characterId: Int): User
    suspend fun purchaseCharacter(characterId: Int, price: Int): User
    suspend fun updateVolume(musicVolume: Float, effectsVolume: Float): User
}