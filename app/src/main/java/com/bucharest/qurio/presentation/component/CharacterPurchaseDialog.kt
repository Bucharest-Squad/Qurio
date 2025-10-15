package com.bucharest.qurio.presentation.component

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.VISIBLE
import android.view.Window
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import com.bucharest.qurio.databinding.CharacterPurchaseDialogBinding
import com.bucharest.qurio.presentation.character_dialog.CharacterUiModel

class CharacterPurchaseDialog(
    val characterUiModel: CharacterUiModel,
    val onBuyButtonClicked: (Int) -> Unit,
    val onCancelButtonClicked: () -> Unit
) : DialogFragment() {
    init {
        Log.d("WOW", "CharacterPurchaseDialog")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = CharacterPurchaseDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                onCancelButtonClicked()
                dialog.hide()
                true
            } else {
                false
            }
        }
        binding.price.text = characterUiModel.characterPrice

        binding.cancelButton.setOnClickListener {
            onCancelButtonClicked()
            dialog.hide()
        }

        binding.buyButton.setOnClickListener {
            onBuyButtonClicked(characterUiModel.id)
            dialog.dismiss()
        }

        binding.lockedIcon.visibility = VISIBLE
        binding.character.setImageResource(characterUiModel.imageRes.second)
        binding.closeButton.setOnClickListener {
            dismiss()
        }
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog.window?.setLayout(
            (328 * (context?.resources?.displayMetrics?.density ?: 30f)).toInt(),
            (273 * (context?.resources?.displayMetrics?.density ?: 30f)).toInt()
        )

        return dialog
    }
}