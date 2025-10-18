package com.bucharest.qurio.presentation.component

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bucharest.qurio.QurioApp
import com.bucharest.qurio.audio.AudioManager
import com.bucharest.qurio.databinding.CharactersDialogBinding
import com.bucharest.qurio.presentation.adapter.CharactersCardAdapter
import com.bucharest.qurio.presentation.character_dialog.CharacterUiModel
import javax.inject.Inject

class CharactersDialog(
    private val currentCharacterId: Int,
    private val charactersUiModel: List<CharacterUiModel>,
    private val onConfirmButtonClicked: (Int) -> Unit,
    private val onBuyButtonClicked: (Int) -> Unit,
    private val onRefreshRequested: (() -> Unit)? = null
) : DialogFragment() {

    @Inject
    lateinit var audioManager: AudioManager

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        (requireActivity().application as QurioApp).appComponent.inject(this)
        
        binding = CharactersDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = com.bucharest.qurio.R.style.DialogAnimation


        dialog.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                dismiss()
                true
            } else {
                false
            }
        }

        val adapter = CharactersCardAdapter(
            selectedCharId = currentCharacterId,
            onCharacterCardClicked = { id ->
                binding.confirmButton.setOnClickListener {
                    audioManager.playButtonPress()
                    onConfirmButtonClicked(id)
                    dismiss()
                }
            },
            onCharacterCardDoubleClicked = { character ->
                CharacterDetailsDialog(
                    characterUiModel = character,
                    onOkButtonClicked = { },
                    onBuyButtonClicked = {
                        onBuyButtonClicked(it)
                    }
                ).show(childFragmentManager, "CharacterDetailsDialog")
            },
        )

        with(binding) {
            characterList.adapter = adapter
            characterList.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.HORIZONTAL, false)
            adapter.submitList(charactersUiModel)

            cancelButton.setOnClickListener { 
                audioManager.playButtonPress()
                dismiss() 
            }
            closeButton.setOnClickListener { 
                audioManager.playButtonPress()
                dismiss() 
            }
        }

        dialog.setContentView(binding.root)

        val density = context?.resources?.displayMetrics?.density ?: 1f
        dialog.window?.setLayout((328 * density).toInt(), (314 * density).toInt())

        return dialog
    }
    
    private lateinit var binding: CharactersDialogBinding
    
    fun refreshCharacterList(newCharacters: List<CharacterUiModel>) {
        if (::binding.isInitialized) {
            (binding.characterList.adapter as? CharactersCardAdapter)?.submitList(newCharacters)
        }
    }
}