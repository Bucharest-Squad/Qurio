package com.bucharest.qurio.presentation.home

import com.bucharest.qurio.presentation.base.BasePresenter

class HomePresenter : BasePresenter<HomeView>() {

    fun onStartQuizClicked() {
        executeIfViewAttached {
            navigateToQuiz()
        }
    }
}