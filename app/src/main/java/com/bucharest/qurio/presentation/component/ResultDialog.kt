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

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val resultCard = ResultCard(requireContext())
        resultCard.setStats(correctCount, incorrectCount, skippedCount, score,starsCount)

        dialog.setContentView(resultCard)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return dialog
    }
}