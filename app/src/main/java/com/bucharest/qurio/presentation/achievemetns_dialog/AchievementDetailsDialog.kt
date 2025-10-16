package com.bucharest.qurio.presentation.achievemetns_dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.bucharest.qurio.databinding.AchievementsDetailsDialogBinding

class AchievementDetailsDialog(
    private val achievementUImodel: AchievementUImodel,
    private val onOkButtonClicked: () -> Unit,
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = AchievementsDetailsDialogBinding.inflate(layoutInflater)
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
            achievementName.text = achievementUImodel.title
            achievementHeadingTitle.text = achievementUImodel.title
            achievementDescription.text = achievementUImodel.description
            achievement.setImageResource(
                if (achievementUImodel.unlocked)
                    achievementUImodel.imageResFilledAndOutlined.first
                else achievementUImodel.imageResFilledAndOutlined.second
            )
            shareButton.visibility = if (achievementUImodel.unlocked) VISIBLE  else GONE
            calibrate.visibility = if (achievementUImodel.unlocked) VISIBLE  else GONE

            okButton.setOnClickListener { handleOk() }
            closeButton.setOnClickListener { dismiss() }

            shareButton.setOnClickListener {
                calibrate.alpha=1f
                shareButton.visibility=GONE
            }
        }

        dialog.setContentView(binding.root)


        val density = context?.resources?.displayMetrics?.density ?: 1f
        dialog.window?.setLayout((328 * density).toInt(), (385 * density).toInt())

        return dialog
    }
}