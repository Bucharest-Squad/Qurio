package com.bucharest.qurio.data.seed

import com.bucharest.qurio.data.local.dto.AchievementDto
import com.bucharest.qurio.data.local.dto.CategoryDto
import com.bucharest.qurio.data.local.dto.CharacterDto
import com.bucharest.qurio.domain.entity.Achievement

class SeedDataProvider {

    fun characters(): List<CharacterDto> = listOf(
        CharacterDto(
            id = 1000,
            name = "Rika",
            description = "Nature's little explorer! Rika talks to mushrooms and swears squirrels give her battle advice. Always ready for an adventure.",
            price = 0,
            owned = true
        ),
        CharacterDto(
            id = 1001,
            name = "Kaiyo",
            description = "A calm storm in human form. Kaiyo trains with ancient scrolls by day and drinks spicy tea by night. Sword sharp, heart sharper.",
            price = 300,
            owned = false
        ),
        CharacterDto(
            id = 1002,
            name = "Mimi",
            description = "Tiny but terrifying! Mimi is always grumpy, but don't let that scare you—unless you like pranks involving firecrackers.",
            price = 700,
            owned = false
        ),
        CharacterDto(
            id = 1003,
            name = "Yoru",
            description = "Quiet, mysterious, and probably watching you right now. Yoru shows up when you least expect it.",
            price = 1000,
            owned = false
        ),
        CharacterDto(
            id = 1004,
            name = "Kuro",
            description = "Cool jacket, cooler moves. Kuro never backs down from a challenge.",
            price = 3000,
            owned = false
        ),
        CharacterDto(
            id = 1005,
            name = "Miko",
            description = "Energetic, cheerful, and faster than a leaf in the wind. Miko can turn any trivia into a giggle-fest.",
            price = 7000,
            owned = false
        ),
        CharacterDto(
            id = 1006,
            name = "Aori",
            description = "The sword chooses the wielder—and it chose Aori. Calm, focused.",
            price = 12000,
            owned = false
        ),
        CharacterDto(
            id = 1007,
            name = "Nara",
            description = "Part magic, part sass. Nara sparkles even when she's mad.",
            price = 30000,
            owned = false
        ),
        CharacterDto(
            id = 1008,
            name = "Renji",
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
        CategoryDto(31, "Entertainment: Japanese Anime & Manga"),
        CategoryDto(32, "Entertainment: Cartoon & Animations")
    )
}