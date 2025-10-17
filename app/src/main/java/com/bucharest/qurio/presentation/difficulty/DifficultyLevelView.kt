package com.bucharest.qurio.presentation.difficulty

import com.bucharest.qurio.domain.entity.Difficulty
import com.bucharest.qurio.presentation.base.BaseView

interface DifficultyLevelView : BaseView {
    fun onEasySelected()
    fun onMediumSelected()
    fun onHardSelected()
    fun setConfirmButtonEnabled(enabled: Boolean)
    fun onConfirmClicked(difficulty: Difficulty)
    fun onCancelClicked()
}
