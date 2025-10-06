package com.bucharest.qurio.component.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import com.bucharest.qurio.R

object CategoriesList {
    fun getCategories(context: Context) =
        listOf(
            CategoryUiModel(
                1,
                getString(context, R.string.category_title_geography),
                R.drawable.category_geography,
                ContextCompat.getColor(context, R.color.category_geography_start),
                ContextCompat.getColor(context, R.color.category_geography_end)
            ),
            CategoryUiModel(
                2,
                getString(context, R.string.category_title_science),
                R.drawable.category_science,
                ContextCompat.getColor(context, R.color.category_geography_start),
                ContextCompat.getColor(context, R.color.category_geography_end)
            ),
            CategoryUiModel(
                3,
                getString(context, R.string.category_title_general_knowledge),
                R.drawable.category_general_knowledge,
                ContextCompat.getColor(context, R.color.category_general_knowledge_start),
                ContextCompat.getColor(context, R.color.category_general_knowledge_end)
            ),
            CategoryUiModel(
                4,
                getString(context, R.string.category_title_music),
                R.drawable.category_music,
                ContextCompat.getColor(context, R.color.category_music_start),
                ContextCompat.getColor(context, R.color.category_music_end)
            ),
            CategoryUiModel(
                5,
                getString(context, R.string.category_title_film_tv),
                R.drawable.category_film_tv,
                ContextCompat.getColor(context, R.color.category_music_start),
                ContextCompat.getColor(context, R.color.category_music_end)
            ),
            CategoryUiModel(
                6,
                getString(context, R.string.category_title_food_drink),
                R.drawable.category_food_drink,
                ContextCompat.getColor(context, R.color.category_food_drink_start),
                ContextCompat.getColor(context, R.color.category_food_drink_end)
            ),
            CategoryUiModel(
                7,
                getString(context, R.string.category_title_society_culture),
                R.drawable.category_society_culture,
                ContextCompat.getColor(context, R.color.category_general_knowledge_start),
                ContextCompat.getColor(context, R.color.category_general_knowledge_end)
            ),
            CategoryUiModel(
                8,
                getString(context, R.string.category_title_sport_leisure),
                R.drawable.category_sport_leisure,
                ContextCompat.getColor(context, R.color.category_food_drink_start),
                ContextCompat.getColor(context, R.color.category_food_drink_end)
            ),
            CategoryUiModel(
                9,
                getString(context, R.string.category_title_history),
                R.drawable.category_history,
                ContextCompat.getColor(context, R.color.category_general_knowledge_start),
                ContextCompat.getColor(context, R.color.category_general_knowledge_end)
            ),
            CategoryUiModel(
                10,
                getString(context, R.string.category_title_arts_literature),
                R.drawable.category_arts_lierature,
                ContextCompat.getColor(context, R.color.category_food_drink_start),
                ContextCompat.getColor(context, R.color.category_food_drink_end)
            )
        )

}