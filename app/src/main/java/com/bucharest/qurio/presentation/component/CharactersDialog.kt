package com.bucharest.qurio.presentation.component

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Window
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bucharest.qurio.databinding.CharactersDialogBinding
import com.bucharest.qurio.presentation.adapter.CharactersCardAdapter
import com.bucharest.qurio.presentation.character_dialog.CharacterUiModel

class CharactersDialog(
    val currentCharacterId: Int,
    val charactersUiModel: List<CharacterUiModel>,
    val onConfirmButtonClicked: (Int) -> Unit,
    val onBuyButtonClicked : (Int) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = CharactersDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val charactersRecyclerView = binding.characterList
        val adapter = CharactersCardAdapter(
            selectedCharId =currentCharacterId,
            onCharacterCardClicked =
                { id ->
                    binding.confirmButton.setOnClickListener {
                        onConfirmButtonClicked(id)
                        dismiss()
                    }
                },
            onCharacterCardDoubleClicked = { character ->
                CharacterDetailsDialog(
                    characterUiModel = character,
                    onOkButtonClicked = {
                        dialog.show()
                    },
                    onBuyButtonClicked ={
                        onBuyButtonClicked(it)
                    }
                ).show(childFragmentManager, "")

                dialog.hide()
            },
        )
        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        charactersRecyclerView.adapter = adapter
        charactersRecyclerView.layoutManager = GridLayoutManager(
            requireContext(),
            2, RecyclerView.HORIZONTAL, false
        )

        adapter.submitList(
            charactersUiModel
        )
        dialog.setContentView(binding.root)

        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog.window?.setLayout(
            (328 * (context?.resources?.displayMetrics?.density ?: 30f)).toInt(),
            (314 * (context?.resources?.displayMetrics?.density ?: 30f)).toInt()
        )

        return dialog
    }
}