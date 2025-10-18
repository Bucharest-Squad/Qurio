package com.bucharest.qurio.presentation.buy_life

import com.bucharest.qurio.domain.entity.Difficulty
import com.bucharest.qurio.domain.repository.UserRepository
import com.bucharest.qurio.presentation.base.BasePresenter
import javax.inject.Inject

class BuyLifePresenter @Inject constructor(
    private val userRepository: UserRepository
) : BasePresenter<BuyLifeView>() {

    private var userCoins: Int = 0
    private val LIFE_COST = 200

    fun onBuyClicked() {
        if (userCoins >= LIFE_COST) {
            executeIfViewAttached {
                onBuyClicked()
            }
        } else {
            executeIfViewAttached {
                showError("Not enough coins! You need 200 coins to buy a life.")
            }
        }
    }

    fun onCancelClicked() {
        executeIfViewAttached {
            onCancelClicked()
        }
    }

    fun loadUserCoins() {
        tryToExecute(
            execute = { userRepository.getUser().coins },
            onSuccess = { coins ->
                userCoins = coins
                executeIfViewAttached {
                    val canBuy = coins >= LIFE_COST
                    updateBuyButtonState(canBuy)
                }
            },
            onError = { throwable ->
                executeIfViewAttached {
                    showError("Failed to load user data")
                }
            }
        )
    }

    private fun updateBuyButtonState(canBuy: Boolean) {
        executeIfViewAttached {
            setBuyButtonEnabled(canBuy)
        }
    }
}
