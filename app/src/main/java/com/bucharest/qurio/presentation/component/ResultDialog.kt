package com.bucharest.qurio.presentation.component

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment

class ResultDialog(
    private val correctCount: Int,
    private val incorrectCount: Int,
    private val skippedCount: Int,
    private val starsCount: Int,
    private val score: Int
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return createResultDialog()
    }
    
    private fun createResultDialog(): Dialog {
        return Dialog(requireContext()).apply {
            configureDialogWindow()
            setDialogContent()
        }
    }
    
    private fun Dialog.configureDialogWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }
    
    private fun Dialog.setDialogContent() {
        val resultCard = createResultCard()
        setContentView(resultCard)
    }
    
    private fun createResultCard(): ResultCard {
        return ResultCard(requireContext()).apply {
            setStats(
                correctCount = correctCount,
                incorrectCount = incorrectCount,
                skippedCount = skippedCount,
                score = score,
                starsCount = starsCount
            )
        }
    }
}
