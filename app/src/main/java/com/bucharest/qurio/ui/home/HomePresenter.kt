package com.bucharest.qurio.ui.home

import com.bucharest.qurio.base.BasePresenter

class HomePresenter : BasePresenter<HomeView>() {

    fun onStartQuizClicked() {
        executeIfViewAttached {
            navigateToQuiz()
        }
    }
}