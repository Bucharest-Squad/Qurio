package com.bucharest.qurio.presentation.home

import android.content.Context
import androidx.core.content.ContextCompat
import com.bucharest.qurio.R
import com.bucharest.qurio.domain.entity.Category

object CategoryMapper {

    fun toUiState(categories: List<Category>, context: Context): List<CategoryState> {
        return categories.mapNotNull { category ->
            getCategoryUiData(category, context)
        }
    }

    private fun getCategoryUiData(category: Category, context: Context): CategoryState? {
        val categoryName = category.name.lowercase()
        
        return when {
            categoryName.contains("geography") -> CategoryState(
                id = category.id,
                title = context.getString(R.string.category_title_geography),
                imageRes = R.drawable.category_geography,
                startColor = ContextCompat.getColor(context, R.color.category_geography_start),
                endColor = ContextCompat.getColor(context, R.color.category_geography_end)
            )
            categoryName.contains("science") -> CategoryState(
                id = category.id,
                title = context.getString(R.string.category_title_science),
                imageRes = R.drawable.category_science,
                startColor = ContextCompat.getColor(context, R.color.category_geography_start),
                endColor = ContextCompat.getColor(context, R.color.category_geography_end)
            )
            categoryName.contains("general") || categoryName.contains("knowledge") -> CategoryState(
                id = category.id,
                title = context.getString(R.string.category_title_general_knowledge),
                imageRes = R.drawable.category_general_knowledge,
                startColor = ContextCompat.getColor(context, R.color.category_general_knowledge_start),
                endColor = ContextCompat.getColor(context, R.color.category_general_knowledge_end)
            )
            categoryName.contains("music") -> CategoryState(
                id = category.id,
                title = context.getString(R.string.category_title_music),
                imageRes = R.drawable.category_music,
                startColor = ContextCompat.getColor(context, R.color.category_music_start),
                endColor = ContextCompat.getColor(context, R.color.category_music_end)
            )
            categoryName.contains("film") || categoryName.contains("tv") -> CategoryState(
                id = category.id,
                title = context.getString(R.string.category_title_film_tv),
                imageRes = R.drawable.category_film_tv,
                startColor = ContextCompat.getColor(context, R.color.category_music_start),
                endColor = ContextCompat.getColor(context, R.color.category_music_end)
            )
            categoryName.contains("food") || categoryName.contains("drink") -> CategoryState(
                id = category.id,
                title = context.getString(R.string.category_title_food_drink),
                imageRes = R.drawable.category_food_drink,
                startColor = ContextCompat.getColor(context, R.color.category_food_drink_start),
                endColor = ContextCompat.getColor(context, R.color.category_food_drink_end)
            )
            categoryName.contains("society") || categoryName.contains("culture") -> CategoryState(
                id = category.id,
                title = context.getString(R.string.category_title_society_culture),
                imageRes = R.drawable.category_society_culture,
                startColor = ContextCompat.getColor(context, R.color.category_general_knowledge_start),
                endColor = ContextCompat.getColor(context, R.color.category_general_knowledge_end)
            )
            categoryName.contains("sport") || categoryName.contains("leisure") -> CategoryState(
                id = category.id,
                title = context.getString(R.string.category_title_sport_leisure),
                imageRes = R.drawable.category_sport_leisure,
                startColor = ContextCompat.getColor(context, R.color.category_food_drink_start),
                endColor = ContextCompat.getColor(context, R.color.category_food_drink_end)
            )
            categoryName.contains("history") -> CategoryState(
                id = category.id,
                title = context.getString(R.string.category_title_history),
                imageRes = R.drawable.category_history,
                startColor = ContextCompat.getColor(context, R.color.category_general_knowledge_start),
                endColor = ContextCompat.getColor(context, R.color.category_general_knowledge_end)
            )
            categoryName.contains("arts") || categoryName.contains("literature") -> CategoryState(
                id = category.id,
                title = context.getString(R.string.category_title_arts_literature),
                imageRes = R.drawable.category_arts_lierature,
                startColor = ContextCompat.getColor(context, R.color.category_food_drink_start),
                endColor = ContextCompat.getColor(context, R.color.category_food_drink_end)
            )
            else -> null // Skip categories without UI mapping
        }
    }
}


