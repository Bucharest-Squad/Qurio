package com.bucharest.qurio.presentation.component

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.bucharest.qurio.databinding.DifficultyLevelDialogBinding

class DifficultyLevelDialog(
    private val onConfirmButtonClicked: (Int) -> Unit,
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DifficultyLevelDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        dialog.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                dismiss()
                true
            } else {
                false
            }
        }



        with(binding) {
            easyButton.setOnClickListener {


            }
            mediumButton.setOnClickListener {


            }
            hardButton.setOnClickListener {


            }
        }

        dialog.setContentView(binding.root)

        val density = context?.resources?.displayMetrics?.density ?: 1f
        dialog.window?.setLayout((328 * density).toInt(), (314 * density).toInt())

        return dialog
    }
}