package com.bucharest.qurio.presentation.achievemetns_dialog

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.bucharest.qurio.R
import com.bucharest.qurio.domain.entity.Achievement

object AchievementMapper {

    private const val ACHIEVEMENT_ID_QUIZ_ROOKIE = 2000
    private const val ACHIEVEMENT_ID_STREAK_STARTER = 2001
    private const val ACHIEVEMENT_ID_LUCKY_GUESS = 2002
    private const val ACHIEVEMENT_ID_EXPLORER = 2003
    private const val ACHIEVEMENT_ID_TRIVIA_CHAMP = 2004
    private const val ACHIEVEMENT_ID_COLLECTOR = 2005
    private const val ACHIEVEMENT_ID_LEGEND = 2006
    private const val ACHIEVEMENT_ID_UNTOUCHABLE = 2007
    private const val ACHIEVEMENT_ID_QUICK_THINKER = 2008
    private const val ACHIEVEMENT_ID_STAR_GAZER = 2009
    private const val ACHIEVEMENT_ID_ANSWER_MASTER = 2010


    fun mapAchievementToUiModel(
        achievement: Achievement,
        context: Context
    ): AchievementUImodel {
        val uiResources = getAchievementResources(achievement.id)

        val gradientPair = uiResources.gradientRes?.let {
            Pair(
                ContextCompat.getColor(context, it.first),
                ContextCompat.getColor(context, it.second)
            )
        }

        return AchievementUImodel(
            id = achievement.id,
            title = achievement.name,
            description = achievement.description,
            unlocked = achievement.isUnlocked,
            imageResFilledAndOutlined = uiResources.imageRes,
            gradient = gradientPair
        )
    }

    private fun getAchievementResources(achievementId: Int): AchievementUiResources {
        return achievementResourcesMap[achievementId] ?: defaultAchievementResources
    }

    private data class AchievementUiResources(
        val imageRes: Pair<Int, Int>,
        @ColorRes val gradientRes: Pair<Int, Int>? = null
    )

    private val achievementResourcesMap = mapOf(
        ACHIEVEMENT_ID_QUIZ_ROOKIE to AchievementUiResources(
            imageRes = Pair(R.drawable.img_achievement_quiz_rookie, R.drawable.img_achievement_quiz_rookie_outlined),
        ),
        ACHIEVEMENT_ID_STREAK_STARTER to AchievementUiResources(
            imageRes = Pair(R.drawable.img_achievement_streak_starter, R.drawable.img_achievement_streak_starter_outlined),
        ),
        ACHIEVEMENT_ID_LUCKY_GUESS to AchievementUiResources(
            imageRes = Pair(R.drawable.img_achievement_lucky_guess, R.drawable.img_achievement_lucky_guess_outlined),
        ),
        ACHIEVEMENT_ID_EXPLORER to AchievementUiResources(
            imageRes = Pair(R.drawable.img_achievement_explorer, R.drawable.img_achievement_explorer_outlined),
        ),
        ACHIEVEMENT_ID_TRIVIA_CHAMP to AchievementUiResources(
            imageRes = Pair(R.drawable.img_achievement_trivia_champ, R.drawable.img_achievement_trivia_champ_outlined),
        ),
        ACHIEVEMENT_ID_COLLECTOR to AchievementUiResources(
            imageRes = Pair(R.drawable.img_achievement_collector, R.drawable.img_achievement_collector_outlined),
        ),
        ACHIEVEMENT_ID_LEGEND to AchievementUiResources(
            imageRes = Pair(R.drawable.img_achievement_legend, R.drawable.img_achievement_legend_outlined),
        ),
        ACHIEVEMENT_ID_UNTOUCHABLE to AchievementUiResources(
            imageRes = Pair(R.drawable.img_achievement_untouchable, R.drawable.img_achievement_untouchable_outlined),
        ),
        ACHIEVEMENT_ID_QUICK_THINKER to AchievementUiResources(
            imageRes = Pair(R.drawable.img_achievement_quick_thinker, R.drawable.img_achievement_quick_thinker_outlined),
        ),

        ACHIEVEMENT_ID_STAR_GAZER to AchievementUiResources(
            imageRes = Pair(R.drawable.img_achievement_explorer, R.drawable.img_achievement_explorer_outlined),
        ),

        ACHIEVEMENT_ID_ANSWER_MASTER to AchievementUiResources(
            imageRes = Pair(R.drawable.img_achievement_collector, R.drawable.img_achievement_collector_outlined),
        )
    )

    private val defaultAchievementResources = AchievementUiResources(
        imageRes = Pair(R.drawable.img_achievement_explorer, R.drawable.img_achievement_explorer_outlined),
        gradientRes = null
    )
}
