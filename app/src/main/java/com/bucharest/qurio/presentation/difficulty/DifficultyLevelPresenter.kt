package com.bucharest.qurio.presentation.difficulty

import com.bucharest.qurio.domain.entity.Difficulty
import com.bucharest.qurio.presentation.base.BasePresenter
import javax.inject.Inject

class DifficultyLevelPresenter @Inject constructor() : BasePresenter<DifficultyLevelView>() {

    private var selectedDifficulty: Difficulty? = null

    fun onEasyClicked() {
        selectedDifficulty = Difficulty.EASY
        executeIfViewAttached {
            onEasySelected()
            setConfirmButtonEnabled(true)
        }
    }

    fun onMediumClicked() {
        selectedDifficulty = Difficulty.MEDIUM
        executeIfViewAttached {
            onMediumSelected()
            setConfirmButtonEnabled(true)
        }
    }

    fun onHardClicked() {
        selectedDifficulty = Difficulty.HARD
        executeIfViewAttached {
            onHardSelected()
            setConfirmButtonEnabled(true)
        }
    }

    fun onConfirmClicked() {
        val difficulty = selectedDifficulty ?: return
        executeIfViewAttached {
            onConfirmClicked(difficulty)
        }
    }

    fun onCancelClicked() {
        executeIfViewAttached {
            onCancelClicked()
        }
    }
}
