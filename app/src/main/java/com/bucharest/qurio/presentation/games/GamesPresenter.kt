package com.bucharest.qurio.presentation.games

import android.content.Context
import com.bucharest.qurio.domain.entity.Category
import com.bucharest.qurio.domain.repository.CategoryRepository
import com.bucharest.qurio.presentation.base.BasePresenter
import com.bucharest.qurio.presentation.home.mapper.CategoryMapper
import com.bucharest.qurio.presentation.home.state.CategoryUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class GamesPresenter @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val context: Context
) : BasePresenter<GamesView>() {

    fun loadGames() {
        view?.showLoading()
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val categories = categoryRepository.getAllCategories()
                val categoryUiModels = CategoryMapper.toUiState(categories, context)
                
                CoroutineScope(Dispatchers.Main).launch {
                    view?.hideLoading()
                    view?.showGames(categoryUiModels)
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    view?.hideLoading()
                    view?.showError("Failed to load games: ${e.message}")
                }
            }
        }
    }

    fun onCategoryClicked(categoryId: Int) {
        view?.navigateToCategoryGame(categoryId)
    }
}
