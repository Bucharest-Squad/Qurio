package com.bucharest.qurio.data.repository

import com.bucharest.qurio.data.local.dao.UserDao
import com.bucharest.qurio.data.local.dao.CharacterDao
import com.bucharest.qurio.data.local.dto.UserDto
import com.bucharest.qurio.data.local.AchievementManager
import com.bucharest.qurio.data.local.mapper.toDto
import com.bucharest.qurio.data.local.mapper.toEntity
import com.bucharest.qurio.domain.entity.User
import com.bucharest.qurio.domain.exception.NotEnoughCoinsException
import com.bucharest.qurio.domain.repository.UserRepository
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDate.Companion.fromEpochDays
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val characterDao: CharacterDao,
    private val achievementManager: AchievementManager
) : UserRepository {

    override suspend fun getUser(): User = 
        userDao.getUser()?.toEntity() ?: createDefaultUser()

    override suspend fun updateCoins(delta: Int): User = 
        updateUser { copy(coins = (coins + delta).coerceAtLeast(MIN_COINS)) }

    override suspend fun updateLives(delta: Int): User = 
        updateUser { copy(lives = (lives + delta).coerceIn(MIN_LIVES, MAX_LIVES)) }

    override suspend fun setActiveCharacter(characterId: Int): User = 
        updateUser { copy(currentCharacterId = characterId) }

    override suspend fun purchaseCharacter(characterId: Int, price: Int): User {
        require(price >= MIN_PRICE) { "Price cannot be negative" }
        
        return updateUser { 
            if (coins < price) throw NotEnoughCoinsException("Not enough coins")
            copy(coins = coins - price)
        }.also {
            characterDao.updateOwnership(characterId, owned = true)
            achievementManager.evaluateAndUnlockAchievements()
        }
    }

    override suspend fun updateVolume(musicVolume: Float, effectsVolume: Float): User = 
        updateUser {
            copy(
                musicVolume = musicVolume.coerceIn(MIN_VOLUME, MAX_VOLUME),
                effectsVolume = effectsVolume.coerceIn(MIN_VOLUME, MAX_VOLUME)
            )
        }

    internal suspend fun updateStreakAfterGamePlayed(): User {
        val today = now()
            .toLocalDateTime(currentSystemDefault())
            .date
        
        return updateUser { 
            calculateStreakUpdate(this, today) 
        }.also {
            achievementManager.evaluateAndUnlockAchievements()
        }
    }

    private suspend inline fun updateUser(transform: User.() -> User): User {
        val updated = getUser().transform()
        userDao.update(toDto(updated))
        return updated
    }

    private suspend fun createDefaultUser(): User {
        val defaultUser = UserDto(
            id = USER_ID,
            currentCharacterId = DEFAULT_CHARACTER_ID,
            coins = INITIAL_COINS,
            lives = DEFAULT_LIVES,
            musicVolume = DEFAULT_VOLUME,
            effectsVolume = DEFAULT_VOLUME,
            lastPlayedEpochDay = null,
            currentDailyStreak = INITIAL_STREAK,
            streakStartEpochDay = null
        )
        userDao.insert(defaultUser)
        characterDao.getById(DEFAULT_CHARACTER_ID)?.takeUnless { it.owned }?.let {
            characterDao.updateOwnership(DEFAULT_CHARACTER_ID, owned = true)
        }
        return defaultUser.toEntity()
    }

    private fun calculateStreakUpdate(user: User, today: LocalDate): User {
        val lastEpoch = user.lastPlayedDate?.toEpochDays()
        val todayEpoch = today.toEpochDays()
        
        val (newStreak, newStartDate) = when {
            lastEpoch == null -> STREAK_RESET_VALUE to today
            lastEpoch == todayEpoch -> user.currentDailyStreak to user.streakStartDate
            todayEpoch - lastEpoch == 1 -> {
                (user.currentDailyStreak + 1).coerceAtMost(DEFAULT_WEEK_MAX_STREAK) to 
                    (user.streakStartDate ?: fromEpochDays(lastEpoch))
            }
            else -> STREAK_RESET_VALUE to today
        }
        
        return user.copy(
            lastPlayedDate = today,
            currentDailyStreak = newStreak,
            streakStartDate = newStartDate
        )
    }

    companion object {
        private const val USER_ID = 1
        private const val DEFAULT_CHARACTER_ID = 1000
        private const val DEFAULT_LIVES = 10
        private const val MAX_LIVES = 100
        private const val MIN_LIVES = 0
        private const val INITIAL_COINS = 0
        private const val MIN_COINS = -99999
        private const val MIN_PRICE = 0
        private const val DEFAULT_VOLUME = 1f
        private const val MIN_VOLUME = 0f
        private const val MAX_VOLUME = 1f
        private const val STREAK_RESET_VALUE = 1
        private const val INITIAL_STREAK = 0
        private const val DEFAULT_WEEK_MAX_STREAK = 7
    }
}