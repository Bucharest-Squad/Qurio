package com.bucharest.qurio.components

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.bucharest.qurio.databinding.ItemResultBinding

class ResultDialog(
    private val correct: Int,
    private val incorrect: Int,
    private val skipped: Int,
    private val stars: Int
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val resultCard = ResultCard(requireContext())
        resultCard.setStats(correct, incorrect, skipped)
        resultCard.setStars(stars)

        dialog.setContentView(resultCard)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return dialog
    }
}