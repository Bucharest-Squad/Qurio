package com.bucharest.qurio.data.seed

import com.bucharest.qurio.data.local.dto.AchievementDto
import com.bucharest.qurio.data.local.dto.CategoryDto
import com.bucharest.qurio.data.local.dto.CharacterDto
import com.bucharest.qurio.data.local.dto.GameSessionDto
import com.bucharest.qurio.domain.entity.Achievement
import com.bucharest.qurio.domain.entity.Difficulty
import java.time.Instant
import java.time.temporal.ChronoUnit

class SeedDataProvider {

    fun characters(): List<CharacterDto> = listOf(
        CharacterDto(
            id = 1000,
            name = "Rika",
            age = "12 Sunblooms",
            description = "Nature's little explorer! Rika talks to mushrooms and swears squirrels give her battle advice. Always ready for an adventure.",
            price = 0,
            owned = true
        ),
        CharacterDto(
            id = 1001,
            name = "Kaiyo",
            age = "14 Storms",
            description = "A calm storm in human form. Kaiyo trains with ancient scrolls by day and drinks spicy tea by night. Sword sharp, heart sharper.",
            price = 300,
            owned = false
        ),
        CharacterDto(
            id = 1002,
            name = "Mimi",
            age = "10 Volcano Puffs",
            description = "Tiny but terrifying! Mimi is always grumpy, but don't let that scare you—unless you like pranks involving firecrackers.",
            price = 700,
            owned = false
        ),
        CharacterDto(
            id = 1003,
            name = "Yoru",
            age = "13 Shadows",
            description = "Quiet, mysterious, and probably watching you right now. Yoru shows up when you least expect it.",
            price = 1000,
            owned = false
        ),
        CharacterDto(
            id = 1004,
            name = "Kuro",
            age = "15 Thunder Beats",
            description = "Cool jacket, cooler moves. Kuro never backs down from a challenge.",
            price = 3000,
            owned = false
        ),
        CharacterDto(
            id = 1005,
            name = "Miko",
            age = "11 Leaf Turns",
            description = "Energetic, cheerful, and faster than a leaf in the wind. Miko can turn any trivia into a giggle-fest.",
            price = 7000,
            owned = false
        ),
        CharacterDto(
            id = 1006,
            name = "Aori",
            age = "13 Blade Echoes",
            description = "The sword chooses the wielder—and it chose Aori. Calm, focused.",
            price = 12000,
            owned = false
        ),
        CharacterDto(
            id = 1007,
            name = "Nara",
            age = "12 Crystal Songs",
            description = "Part magic, part sass. Nara sparkles even when she's mad.",
            price = 30000,
            owned = false
        ),
        CharacterDto(
            id = 1008,
            name = "Renji",
            age = "11 Hero Coins",
            description = "Small but mighty! Renji dreams of glory, carries a shield too big for him.",
            price = 50000,
            owned = false
        )
    )

    fun achievements(): List<AchievementDto> = listOf(
        AchievementDto(
            id = 2000,
            name = "Quiz Rookie",
            description = "Start your journey! Complete your very first trivia game to prove you're ready for adventure.",
            criteria = Achievement.Criteria.GamesPlayedAtLeast(1),
            unlocked = true
        ),
        AchievementDto(
            id = 2001,
            name = "Streak Starter",
            description = "Consistency is power. Achieve a streak of 3 correct answers in a row during a single game.",
            criteria = Achievement.Criteria.CorrectStreakAtLeast(3),
            unlocked = false
        ),
        AchievementDto(
            id = 2002,
            name = "Lucky Guess",
            description = "Lightning reflexes! Answer any question correctly in under 2 seconds from when it appears.",
            criteria = Achievement.Criteria.FastAnswerUnderSeconds(2),
            unlocked = false
        ),
        AchievementDto(
            id = 2003,
            name = "Explorer",
            description = "Broaden your horizons. Play games across at least 4 different categories to show your curiosity.",
            criteria = Achievement.Criteria.CategoriesPlayedAtLeast(4),
            unlocked = false
        ),
        AchievementDto(
            id = 2004,
            name = "Trivia Champ",
            description = "You've got brains and speed. Score at least 1000 points in a single game session.",
            criteria = Achievement.Criteria.ScoreAtLeast(1000),
            unlocked = false
        ),
        AchievementDto(
            id = 2005,
            name = "Collector",
            description = "Build your squad. Own at least 5 playable characters in the store.",
            criteria = Achievement.Criteria.OwnedCharactersAtLeast(5),
            unlocked = false
        ),
        AchievementDto(
            id = 2006,
            name = "Legend",
            description = "A true veteran. Complete at least 50 games over time.",
            criteria = Achievement.Criteria.GamesPlayedAtLeast(50),
            unlocked = false
        ),
        AchievementDto(
            id = 2007,
            name = "Untouchable",
            description = "Flawless victory. Finish a game without giving a single wrong answer.",
            criteria = Achievement.Criteria.NoWrongAnswersInGame,
            unlocked = false
        ),
        AchievementDto(
            id = 2008,
            name = "Quick Thinker",
            description = "Sharp and accurate. Answer 10 questions correctly where each answer was under 5 seconds.",
            criteria = Achievement.Criteria.FastAnswerUnderSeconds(5),
            unlocked = false
        ),
        AchievementDto(
            id = 2009,
            name = "Star Gazer",
            description = "Shine bright. Accumulate at least 30 stars across your game history.",
            criteria = Achievement.Criteria.StarsEarnedAtLeast(30),
            unlocked = false
        ),
        AchievementDto(
            id = 2010,
            name = "Answer Master",
            description = "Knowledge powerhouse. Answer at least 200 questions correctly across all games.",
            criteria = Achievement.Criteria.CorrectAnswersAtLeast(200),
            unlocked = false
        )
    )

    fun categories(): List<CategoryDto> = listOf(
        CategoryDto(9, "General Knowledge"),
        CategoryDto(10, "Entertainment: Books"),
        CategoryDto(11, "Entertainment: Film"),
        CategoryDto(12, "Entertainment: Music"),
        CategoryDto(13, "Entertainment: Musicals & Theatres"),
        CategoryDto(14, "Entertainment: Television"),
        CategoryDto(15, "Entertainment: Video Games"),
        CategoryDto(16, "Entertainment: Board Games"),
        CategoryDto(17, "Science & Nature"),
        CategoryDto(18, "Science: Computers"),
        CategoryDto(19, "Science: Mathematics"),
        CategoryDto(20, "Mythology"),
        CategoryDto(21, "Sports"),
        CategoryDto(22, "Geography"),
        CategoryDto(23, "History"),
        CategoryDto(24, "Politics"),
        CategoryDto(25, "Art"),
        CategoryDto(26, "Celebrities"),
        CategoryDto(27, "Animals"),
        CategoryDto(28, "Vehicles"),
        CategoryDto(29, "Entertainment: Comics"),
        CategoryDto(30, "Science: Gadgets"),
        CategoryDto(31, "Entertainment: Anime & Manga"),
        CategoryDto(32, "Entertainment: Cartoon")
    )
    
    fun gameSessions(): List<GameSessionDto> {
        val now = Instant.now()
        
        return listOf(
            GameSessionDto(
                id = 3000,
                categoryId = 22, // Geography
                difficulty = Difficulty.EASY,
                startedAt = now.minus(2, ChronoUnit.DAYS),
                finishedAt = now.minus(2, ChronoUnit.DAYS).plusSeconds(143), // 2m 23sec
                totalQuestions = 10,
                correctAnswers = 8,
                wrongAnswers = 2,
                skippedAnswers = 0,
                starsEarned = 3,
                coinsEarned = 100,
                livesLost = 2,
                totalScore = 850,
                fastestAnswerSeconds = 3
            ),
            GameSessionDto(
                id = 3001,
                categoryId = 17, // Science & Nature
                difficulty = Difficulty.MEDIUM,
                startedAt = now.minus(1, ChronoUnit.DAYS),
                finishedAt = now.minus(1, ChronoUnit.DAYS).plusSeconds(195), // 3m 15sec
                totalQuestions = 10,
                correctAnswers = 7,
                wrongAnswers = 3,
                skippedAnswers = 0,
                starsEarned = 2,
                coinsEarned = 75,
                livesLost = 3,
                totalScore = 650,
                fastestAnswerSeconds = 4
            ),
            GameSessionDto(
                id = 3002,
                categoryId = 9, // General Knowledge
                difficulty = Difficulty.HARD,
                startedAt = now.minus(3, ChronoUnit.HOURS),
                finishedAt = now.minus(3, ChronoUnit.HOURS).plusSeconds(180), // 3m
                totalQuestions = 10,
                correctAnswers = 9,
                wrongAnswers = 1,
                skippedAnswers = 0,
                starsEarned = 3,
                coinsEarned = 150,
                livesLost = 1,
                totalScore = 1050,
                fastestAnswerSeconds = 2
            ),
            GameSessionDto(
                id = 3003,
                categoryId = 12, // Music
                difficulty = Difficulty.EASY,
                startedAt = now.minus(5, ChronoUnit.DAYS),
                finishedAt = now.minus(5, ChronoUnit.DAYS).plusSeconds(165), // 2m 45sec
                totalQuestions = 10,
                correctAnswers = 6,
                wrongAnswers = 4,
                skippedAnswers = 0,
                starsEarned = 2,
                coinsEarned = 60,
                livesLost = 4,
                totalScore = 550,
                fastestAnswerSeconds = 5
            ),
            GameSessionDto(
                id = 3004,
                categoryId = 23, // History
                difficulty = Difficulty.MEDIUM,
                startedAt = now.minus(1, ChronoUnit.HOURS),
                finishedAt = now.minus(1, ChronoUnit.HOURS).plusSeconds(210), // 3m 30sec
                totalQuestions = 10,
                correctAnswers = 10,
                wrongAnswers = 0,
                skippedAnswers = 0,
                starsEarned = 3,
                coinsEarned = 200,
                livesLost = 0,
                totalScore = 1200,
                fastestAnswerSeconds = 2
            )
        )
    }
}