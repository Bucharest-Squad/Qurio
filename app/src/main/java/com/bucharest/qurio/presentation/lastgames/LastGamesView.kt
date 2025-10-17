package com.bucharest.qurio.presentation.lastgames

import com.bucharest.qurio.presentation.base.BaseView
import com.bucharest.qurio.presentation.home.state.GameSessionUiModel

interface LastGamesView : BaseView {
    fun showGames(games: List<GameSessionUiModel>)
    override fun showLoading()
    override fun hideLoading()
    override fun showError(message: String)
    override fun showMessage(message: String)
}
