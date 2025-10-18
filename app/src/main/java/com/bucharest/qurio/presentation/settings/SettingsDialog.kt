package com.bucharest.qurio.presentation.settings

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.Window
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bucharest.qurio.R
import com.bucharest.qurio.databinding.SettingsDialogBinding
import com.bucharest.qurio.QurioApp
import com.bucharest.qurio.audio.AudioManager
import com.bucharest.qurio.domain.repository.SettingsRepository
import androidx.core.graphics.drawable.toDrawable

class SettingsDialog : DialogFragment(), SettingsView {

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var audioManager: AudioManager

    private lateinit var binding: SettingsDialogBinding
    private lateinit var presenter: SettingsPresenter
    private lateinit var soundSeekBar: SeekBar
    private lateinit var musicSeekBar: SeekBar

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        binding = SettingsDialogBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.root.isSoundEffectsEnabled = false

        val appComponent = (requireActivity().application as QurioApp).appComponent
        settingsRepository = appComponent.getSettingsRepository()
        audioManager = appComponent.getAudioManager()

        presenter = SettingsPresenter(settingsRepository, audioManager)
        presenter.attachView(this)

        setupViews()
        setupSeekBars()
        setupButtons()

        dialog.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                presenter.onDiscardClicked()
                true
            } else {
                false
            }
        }

        val density = context?.resources?.displayMetrics?.density ?: 1f
        dialog.window?.setLayout(
            (328 * density).toInt(),
            (400 * density).toInt()
        )

        return dialog
    }

    private fun setupViews() {
        soundSeekBar = binding.seekbarSound
        musicSeekBar = binding.seekbarMusic

        val (sound, music) = presenter.getCurrentLevels()
        soundSeekBar.progress = sound
        musicSeekBar.progress = music
    }

    private fun setupSeekBars() {
        soundSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    presenter.onSoundChanged(progress)
                    audioManager.updateVolumeLevels()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        musicSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    presenter.onMusicChanged(progress)
                    audioManager.updateVolumeLevels()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupButtons() {
        binding.btnSaveChanges.setOnClickListener {
            audioManager.playButtonPress()
            presenter.onSaveClicked()
        }
        binding.btnDiscardChanges.setOnClickListener {
            audioManager.playButtonPress()
            presenter.onDiscardClicked()
        }
        binding.closeShape.setOnClickListener {
            audioManager.playButtonPress()
            presenter.onDiscardClicked()
        }
    }

    override fun updateSoundLevel(level: Int) {
        soundSeekBar.progress = level
    }

    override fun updateMusicLevel(level: Int) {
        musicSeekBar.progress = level
    }

    override fun showSavedMessage() {
        Toast.makeText(requireContext(), getString(R.string.settings_saved), Toast.LENGTH_SHORT)
            .show()
    }

    override fun showDiscardedMessage() {
        Toast.makeText(requireContext(), getString(R.string.discarded), Toast.LENGTH_SHORT).show()
    }

    override fun dismissDialog() {
        dismiss()
    }

    override fun showLoading() {}

    override fun hideLoading() {}

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::presenter.isInitialized) {
            presenter.detachView()
        }
    }
}