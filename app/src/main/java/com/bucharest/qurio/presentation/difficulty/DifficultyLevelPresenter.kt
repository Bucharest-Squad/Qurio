package com.bucharest.qurio.presentation.difficulty

import com.bucharest.qurio.domain.entity.Difficulty
import com.bucharest.qurio.presentation.base.BasePresenter
import javax.inject.Inject

class DifficultyLevelPresenter @Inject constructor() : BasePresenter<DifficultyLevelView>() {

    private var selectedDifficulty: Difficulty? = null

    fun onEasyClicked() {
        android.util.Log.d("DifficultyPresenter", "onEasyClicked called")
        selectedDifficulty = Difficulty.EASY
        executeIfViewAttached {
            onEasySelected()
            setConfirmButtonEnabled(true)
        }
    }

    fun onMediumClicked() {
        android.util.Log.d("DifficultyPresenter", "onMediumClicked called")
        selectedDifficulty = Difficulty.MEDIUM
        executeIfViewAttached {
            onMediumSelected()
            setConfirmButtonEnabled(true)
        }
    }

    fun onHardClicked() {
        android.util.Log.d("DifficultyPresenter", "onHardClicked called")
        selectedDifficulty = Difficulty.HARD
        executeIfViewAttached {
            onHardSelected()
            setConfirmButtonEnabled(true)
        }
    }

    fun onConfirmClicked() {
        android.util.Log.d("DifficultyPresenter", "onConfirmClicked called, selectedDifficulty: $selectedDifficulty")
        val difficulty = selectedDifficulty ?: return
        executeIfViewAttached {
            onConfirmClicked(difficulty)
        }
    }

    fun onCancelClicked() {
        android.util.Log.d("DifficultyPresenter", "onCancelClicked called")
        executeIfViewAttached {
            onCancelClicked()
        }
    }
}
