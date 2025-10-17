package com.bucharest.qurio.presentation.buy_life

import com.bucharest.qurio.domain.entity.Difficulty
import com.bucharest.qurio.presentation.base.BaseView

interface BuyLifeView : BaseView {

    fun onBuyClicked()
    fun onCancelClicked()
}
