package com.bucharest.qurio.presentation.achievemetns_dialog

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
import com.bucharest.qurio.databinding.AchievementsDialogBinding
import com.bucharest.qurio.presentation.adapter.AchievementsAdapter
import javax.inject.Inject

class AchievementsDialog(
    private val achievementsUiModelList: List<AchievementUImodel>,
) : DialogFragment() {

    @Inject
    lateinit var audioManager: AudioManager

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        (requireActivity().application as QurioApp).appComponent.inject(this)
        
        val binding = AchievementsDialogBinding.inflate(layoutInflater)
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

        val adapter = AchievementsAdapter(
            onCharacterCardClicked = { achievement ->
                    AchievementDetailsDialog(
                        achievementUImodel = achievement,
                        onOkButtonClicked = { dialog.show() },

                    ).show(childFragmentManager, "AchievementDetailsDialog")
                    dialog.hide()
            },
            audioManager = audioManager
        )

        with(binding) {
            characterList.adapter = adapter
            characterList.layoutManager =
                GridLayoutManager(requireContext(), 3, RecyclerView.HORIZONTAL, false)
            adapter.submitList(achievementsUiModelList)
            okButton.setOnClickListener { 
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
        dialog.window?.setLayout((328 * density).toInt(), (482 * density).toInt())

        return dialog
    }
}