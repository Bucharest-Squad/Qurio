package com.bucharest.qurio.presentation.home

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.bucharest.qurio.R
import com.bucharest.qurio.domain.entity.Category

object CategoryMapper {

    private const val CATEGORY_ID_GENERAL_KNOWLEDGE = 9
    private const val CATEGORY_ID_BOOKS = 10
    private const val CATEGORY_ID_FILM = 11
    private const val CATEGORY_ID_MUSIC = 12
    private const val CATEGORY_ID_MUSICALS_THEATRES = 13
    private const val CATEGORY_ID_TELEVISION = 14
    private const val CATEGORY_ID_VIDEO_GAMES = 15
    private const val CATEGORY_ID_BOARD_GAMES = 16
    private const val CATEGORY_ID_SCIENCE_NATURE = 17
    private const val CATEGORY_ID_COMPUTERS = 18
    private const val CATEGORY_ID_MATHEMATICS = 19
    private const val CATEGORY_ID_MYTHOLOGY = 20
    private const val CATEGORY_ID_SPORTS = 21
    private const val CATEGORY_ID_GEOGRAPHY = 22
    private const val CATEGORY_ID_HISTORY = 23
    private const val CATEGORY_ID_POLITICS = 24
    private const val CATEGORY_ID_ART = 25
    private const val CATEGORY_ID_CELEBRITIES = 26
    private const val CATEGORY_ID_ANIMALS = 27
    private const val CATEGORY_ID_VEHICLES = 28
    private const val CATEGORY_ID_COMICS = 29
    private const val CATEGORY_ID_GADGETS = 30
    private const val CATEGORY_ID_ANIME_MANGA = 31
    private const val CATEGORY_ID_CARTOONS_ANIMATIONS = 32

    private const val CATEGORY_SEPARATOR = ":"

    fun toUiState(categories: List<Category>, context: Context): List<CategoryState> {
        return categories.map { category ->
            mapCategoryToUiState(category, context)
        }
    }

    private fun mapCategoryToUiState(category: Category, context: Context): CategoryState {
        val uiResources = getCategoryResources(category.id)
        val displayTitle = extractDisplayTitle(category.name)
        
        return CategoryState(
            id = category.id,
            title = displayTitle,
            imageRes = uiResources.imageRes,
            startColor = ContextCompat.getColor(context, uiResources.startColorRes),
            endColor = ContextCompat.getColor(context, uiResources.endColorRes)
        )
    }

    private fun extractDisplayTitle(fullName: String): String {
        return if (fullName.contains(CATEGORY_SEPARATOR)) {
            fullName.substringAfter(CATEGORY_SEPARATOR).trim()
        } else {
            fullName
        }
    }

    private fun getCategoryResources(categoryId: Int): CategoryUiResources {
        return categoryResourcesMap[categoryId] ?: defaultCategoryResources
    }

    private val categoryResourcesMap = mapOf(
        CATEGORY_ID_GENERAL_KNOWLEDGE to CategoryUiResources(
            imageRes = R.drawable.category_general_knowledge,
            startColorRes = R.color.category_general_knowledge_start,
            endColorRes = R.color.category_general_knowledge_end
        ),
        CATEGORY_ID_BOOKS to CategoryUiResources(
            imageRes = R.drawable.category_arts_lierature,
            startColorRes = R.color.category_food_drink_start,
            endColorRes = R.color.category_food_drink_end
        ),
        CATEGORY_ID_FILM to CategoryUiResources(
            imageRes = R.drawable.category_film_tv,
            startColorRes = R.color.category_music_start,
            endColorRes = R.color.category_music_end
        ),
        CATEGORY_ID_MUSIC to CategoryUiResources(
            imageRes = R.drawable.category_music,
            startColorRes = R.color.category_music_start,
            endColorRes = R.color.category_music_end
        ),
        CATEGORY_ID_MUSICALS_THEATRES to CategoryUiResources(
            imageRes = R.drawable.category_music,
            startColorRes = R.color.category_music_start,
            endColorRes = R.color.category_music_end
        ),
        CATEGORY_ID_TELEVISION to CategoryUiResources(
            imageRes = R.drawable.category_film_tv,
            startColorRes = R.color.category_music_start,
            endColorRes = R.color.category_music_end
        ),
        CATEGORY_ID_VIDEO_GAMES to CategoryUiResources(
            imageRes = R.drawable.category_general_knowledge,
            startColorRes = R.color.category_general_knowledge_start,
            endColorRes = R.color.category_general_knowledge_end
        ),
        CATEGORY_ID_BOARD_GAMES to CategoryUiResources(
            imageRes = R.drawable.category_general_knowledge,
            startColorRes = R.color.category_general_knowledge_start,
            endColorRes = R.color.category_general_knowledge_end
        ),
        CATEGORY_ID_SCIENCE_NATURE to CategoryUiResources(
            imageRes = R.drawable.category_science,
            startColorRes = R.color.category_geography_start,
            endColorRes = R.color.category_geography_end
        ),
        CATEGORY_ID_COMPUTERS to CategoryUiResources(
            imageRes = R.drawable.category_science,
            startColorRes = R.color.category_geography_start,
            endColorRes = R.color.category_geography_end
        ),
        CATEGORY_ID_MATHEMATICS to CategoryUiResources(
            imageRes = R.drawable.category_science,
            startColorRes = R.color.category_geography_start,
            endColorRes = R.color.category_geography_end
        ),
        CATEGORY_ID_MYTHOLOGY to CategoryUiResources(
            imageRes = R.drawable.category_history,
            startColorRes = R.color.category_general_knowledge_start,
            endColorRes = R.color.category_general_knowledge_end
        ),
        CATEGORY_ID_SPORTS to CategoryUiResources(
            imageRes = R.drawable.category_sport_leisure,
            startColorRes = R.color.category_food_drink_start,
            endColorRes = R.color.category_food_drink_end
        ),
        CATEGORY_ID_GEOGRAPHY to CategoryUiResources(
            imageRes = R.drawable.category_geography,
            startColorRes = R.color.category_geography_start,
            endColorRes = R.color.category_geography_end
        ),
        CATEGORY_ID_HISTORY to CategoryUiResources(
            imageRes = R.drawable.category_history,
            startColorRes = R.color.category_general_knowledge_start,
            endColorRes = R.color.category_general_knowledge_end
        ),
        CATEGORY_ID_POLITICS to CategoryUiResources(
            imageRes = R.drawable.category_society_culture,
            startColorRes = R.color.category_general_knowledge_start,
            endColorRes = R.color.category_general_knowledge_end
        ),
        CATEGORY_ID_ART to CategoryUiResources(
            imageRes = R.drawable.category_arts_lierature,
            startColorRes = R.color.category_food_drink_start,
            endColorRes = R.color.category_food_drink_end
        ),
        CATEGORY_ID_CELEBRITIES to CategoryUiResources(
            imageRes = R.drawable.category_film_tv,
            startColorRes = R.color.category_music_start,
            endColorRes = R.color.category_music_end
        ),
        CATEGORY_ID_ANIMALS to CategoryUiResources(
            imageRes = R.drawable.category_science,
            startColorRes = R.color.category_geography_start,
            endColorRes = R.color.category_geography_end
        ),
        CATEGORY_ID_VEHICLES to CategoryUiResources(
            imageRes = R.drawable.category_general_knowledge,
            startColorRes = R.color.category_general_knowledge_start,
            endColorRes = R.color.category_general_knowledge_end
        ),
        CATEGORY_ID_COMICS to CategoryUiResources(
            imageRes = R.drawable.category_arts_lierature,
            startColorRes = R.color.category_food_drink_start,
            endColorRes = R.color.category_food_drink_end
        ),
        CATEGORY_ID_GADGETS to CategoryUiResources(
            imageRes = R.drawable.category_science,
            startColorRes = R.color.category_geography_start,
            endColorRes = R.color.category_geography_end
        ),
        CATEGORY_ID_ANIME_MANGA to CategoryUiResources(
            imageRes = R.drawable.category_film_tv,
            startColorRes = R.color.category_music_start,
            endColorRes = R.color.category_music_end
        ),
        CATEGORY_ID_CARTOONS_ANIMATIONS to CategoryUiResources(
            imageRes = R.drawable.category_film_tv,
            startColorRes = R.color.category_music_start,
            endColorRes = R.color.category_music_end
        )
    )

    private val defaultCategoryResources = CategoryUiResources(
        imageRes = R.drawable.category_general_knowledge,
        startColorRes = R.color.category_general_knowledge_start,
        endColorRes = R.color.category_general_knowledge_end
    )

    private data class CategoryUiResources(
        @DrawableRes val imageRes: Int,
        @ColorRes val startColorRes: Int,
        @ColorRes val endColorRes: Int
    )
}
