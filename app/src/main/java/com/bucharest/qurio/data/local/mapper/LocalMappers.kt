package com.bucharest.qurio.data.local.mapper

import com.bucharest.qurio.data.local.dto.AchievementDto
import com.bucharest.qurio.data.local.dto.CategoryDto
import com.bucharest.qurio.data.local.dto.CharacterDto
import com.bucharest.qurio.data.local.dto.GameSessionDto
import com.bucharest.qurio.data.local.dto.UserDto
import com.bucharest.qurio.domain.entity.Achievement
import com.bucharest.qurio.domain.entity.Category
import com.bucharest.qurio.domain.entity.Character
import com.bucharest.qurio.domain.entity.GameSession
import com.bucharest.qurio.domain.entity.User
import kotlinx.datetime.LocalDate

fun UserDto.toEntity(): User =
    User(
        id = id,
        currentCharacterId = currentCharacterId,
        coins = coins,
        lives = lives,
        lastPlayedDate = lastPlayedEpochDay?.let { LocalDate.fromEpochDays(it.toInt()) },
        currentDailyStreak = currentDailyStreak,
        streakStartDate = streakStartEpochDay?.let { LocalDate.fromEpochDays(it.toInt()) },
        musicVolume = musicVolume,
        effectsVolume = effectsVolume
    )

fun toDto(user: User): UserDto =
    UserDto(
        id = user.id,
        currentCharacterId = user.currentCharacterId,
        coins = user.coins,
        lives = user.lives,
        musicVolume = user.musicVolume,
        effectsVolume = user.effectsVolume,
        lastPlayedEpochDay = user.lastPlayedDate?.toEpochDays()?.toLong(),
        currentDailyStreak = user.currentDailyStreak,
        streakStartEpochDay = user.streakStartDate?.toEpochDays()?.toLong()
    )

fun CharacterDto.toEntity() = Character(id, name, age, description, price, owned)

fun Character.toDto() = CharacterDto(id, name, age, description, price, isOwned)

fun AchievementDto.toEntity() = Achievement(id, name, description, criteria, unlocked)

fun Achievement.toDto() = AchievementDto(id, name, description, criteria, isUnlocked)

fun CategoryDto.toEntity() = Category(id, name)

fun Category.toDto() = CategoryDto(id, name)

fun GameSessionDto.toEntity(category: Category): GameSession =
    GameSession(
        id = id,
        category = category,
        difficulty = difficulty,
        startedAt = startedAt,
        finishedAt = finishedAt,
        totalQuestions = totalQuestions,
        correctAnswers = correctAnswers,
        wrongAnswers = wrongAnswers,
        skippedAnswers = skippedAnswers,
        starsEarned = starsEarned,
        coinsEarned = coinsEarned,
        livesLost = livesLost,
        totalScore = totalScore,
        fastestAnswerSeconds = fastestAnswerSeconds
    )

fun GameSession.toDto(): GameSessionDto =
    GameSessionDto(
        id = id,
        categoryId = category.id,
        difficulty = difficulty,
        startedAt = startedAt,
        finishedAt = finishedAt,
        totalQuestions = totalQuestions,
        correctAnswers = correctAnswers,
        wrongAnswers = wrongAnswers,
        skippedAnswers = skippedAnswers,
        starsEarned = starsEarned,
        coinsEarned = coinsEarned,
        livesLost = livesLost,
        totalScore = totalScore,
        fastestAnswerSeconds = fastestAnswerSeconds
    )