package com.bucharest.qurio.presentation.games

import com.bucharest.qurio.presentation.base.BaseView
import com.bucharest.qurio.presentation.home.state.CategoryUiModel

interface GamesView : BaseView {
    fun showGames(categories: List<CategoryUiModel>)
    fun navigateToCategoryGame(categoryId: Int)
}
