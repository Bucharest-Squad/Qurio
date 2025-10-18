package com.bucharest.qurio.presentation.component

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.bucharest.qurio.QurioApp
import com.bucharest.qurio.audio.AudioManager
import com.bucharest.qurio.databinding.CharactersDetailsDialogBinding
import com.bucharest.qurio.presentation.character_dialog.CharacterUiModel
import javax.inject.Inject

class CharacterDetailsDialog(
    private val characterUiModel: CharacterUiModel,
    private val onOkButtonClicked: () -> Unit,
    private val onBuyButtonClicked: (Int) -> Unit,
) : DialogFragment() {

    @Inject
    lateinit var audioManager: AudioManager

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        (requireActivity().application as QurioApp).appComponent.inject(this)
        
        val binding = CharactersDetailsDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val handleOk = {
            onOkButtonClicked()
            dialog.hide()
        }

        dialog.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                handleOk()
                true
            } else {
                false
            }
        }

        with(binding) {
            characterName.text = characterUiModel.characterName
            characterAge.text = characterUiModel.characterAge
            characterDescription.text = characterUiModel.characterDescription
            character.setImageResource(characterUiModel.imageRes.second)
            buyButton.visibility = if (characterUiModel.isOwned) GONE else VISIBLE
            lockedIcon.visibility = if (characterUiModel.isOwned) GONE else VISIBLE

            okButton.setOnClickListener { 
                audioManager.playButtonPress()
                handleOk() 
            }
            closeButton.setOnClickListener { 
                audioManager.playButtonPress()
                dismiss() 
            }

            buyButton.setOnClickListener {
                audioManager.playButtonPress()
                dialog.hide()
                CharacterPurchaseDialog(
                    characterUiModel = characterUiModel,
                    onBuyButtonClicked = {
                        onBuyButtonClicked(it)
                    },
                    onCancelButtonClicked = {
                        dialog.show()
                    }
                ).show(parentFragmentManager, "CharacterPurchaseDialog")
            }
        }

        dialog.setContentView(binding.root)


        val density = context?.resources?.displayMetrics?.density ?: 1f
        dialog.window?.setLayout((328 * density).toInt(), (314 * density).toInt())

        return dialog
    }
}