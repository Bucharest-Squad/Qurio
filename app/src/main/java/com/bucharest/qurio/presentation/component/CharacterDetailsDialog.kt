package com.bucharest.qurio.presentation.component

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import com.bucharest.qurio.databinding.CharactersDetailsDialogBinding
import com.bucharest.qurio.presentation.character_dialog.CharacterUiModel

class CharacterDetailsDialog(
    val characterUiModel: CharacterUiModel,
    val onOkButtonClicked:()->Unit,
    val onBuyButtonClicked: (Int) -> Unit,
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding= CharactersDetailsDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                onOkButtonClicked()
                dismiss()
                true
            } else {
                false
            }
        }


        binding.okButton.setOnClickListener {
            dismiss()
            onOkButtonClicked()
        }
        binding.buyButton.visibility = if (characterUiModel.isOwned) GONE else VISIBLE

        binding.buyButton.setOnClickListener {
            dialog.hide()
            CharacterPurchaseDialog(
                characterUiModel = characterUiModel,
                onBuyButtonClicked ={
                    onBuyButtonClicked(it)
                    dialog.hide()
                },
                onCancelButtonClicked = {
                    dialog.show()
                }
            ).show(parentFragmentManager,"CharacterPurchaseDialog")
        }
        binding.lockedIcon.visibility = if (characterUiModel.isOwned)  GONE else VISIBLE
        binding.characterName.text = characterUiModel.characterName
        binding.characterAge.text = characterUiModel.characterAge
        binding.characterDescription.text = characterUiModel.characterDescription
        binding.character.setImageResource(characterUiModel.imageRes.second)


        dialog.setContentView(binding.root)

        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog.window?.setLayout(
            (328 * (context?.resources?.displayMetrics?.density ?:30f )).toInt(),
            (314 * (context?.resources?.displayMetrics?.density ?: 30f)).toInt()
        )

        return dialog
    }

}