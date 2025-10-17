package com.bucharest.qurio.presentation.onboarding

import com.bucharest.qurio.presentation.base.BaseView

interface OnBoardingView : BaseView {
    fun onRightArrowClicked()
    fun onLeftArrowClicked()
    fun onSwipeUp()
}