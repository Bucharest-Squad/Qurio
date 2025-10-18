package com.bucharest.qurio.presentation.component

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.View.VISIBLE
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.bucharest.qurio.QurioApp
import com.bucharest.qurio.audio.AudioManager
import com.bucharest.qurio.databinding.CharacterPurchaseDialogBinding
import com.bucharest.qurio.presentation.character_dialog.CharacterUiModel
import javax.inject.Inject

class CharacterPurchaseDialog(
    private val characterUiModel: CharacterUiModel,
    private val onBuyButtonClicked: (Int) -> Unit,
    private val onCancelButtonClicked: () -> Unit
) : DialogFragment() {

    @Inject
    lateinit var audioManager: AudioManager

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        (requireActivity().application as QurioApp).appComponent.inject(this)
        
        val binding = CharacterPurchaseDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val handleCancel = {
            onCancelButtonClicked()
            dialog.hide()
        }

        dialog.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                handleCancel()
                true
            } else {
                false
            }
        }

        with(binding) {
            price.text = characterUiModel.characterPrice
            lockedIcon.visibility = VISIBLE
            character.setImageResource(characterUiModel.imageRes.second)

            cancelButton.setOnClickListener { 
                audioManager.playButtonPress()
                handleCancel() 
            }
            closeButton.setOnClickListener { 
                audioManager.playButtonPress()
                handleCancel() 
            }

            buyButton.setOnClickListener {
                audioManager.playButtonPress()
                onBuyButtonClicked(characterUiModel.id)
                dismiss()
            }
        }

        dialog.setContentView(binding.root)
        val density = context?.resources?.displayMetrics?.density ?: 1f
        dialog.window?.setLayout((328 * density).toInt(), (273 * density).toInt())

        return dialog
    }
}