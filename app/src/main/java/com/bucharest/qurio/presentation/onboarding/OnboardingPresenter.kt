package com.bucharest.qurio.presentation.onboarding

import com.bucharest.qurio.domain.repository.UserPreferences
import com.bucharest.qurio.presentation.base.BasePresenter
import jakarta.inject.Inject

class OnBoardingPresenter @Inject constructor(
    private val userPreferences: UserPreferences
) : BasePresenter<OnBoardingView>() {
    fun setFirstLaunch() {
        tryToExecute(
            execute = { userPreferences.setFirstLaunch() },
            onSuccess = { view?.navigateToHome() },
            onError = { view?.showError(it.message.toString()) }
        )
    }
}