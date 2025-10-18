package com.bucharest.qurio.presentation.buy_life

import com.bucharest.qurio.domain.entity.Difficulty
import com.bucharest.qurio.presentation.base.BasePresenter
import javax.inject.Inject

class BuyLifePresenter @Inject constructor() : BasePresenter<BuyLifeView>() {


    fun onBuyClicked() {
        executeIfViewAttached {
            onBuyClicked()
        }
    }
    fun onCancelClicked() {
        executeIfViewAttached {
            onCancelClicked()
        }
    }
}
