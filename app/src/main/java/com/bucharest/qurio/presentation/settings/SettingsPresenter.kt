package com.bucharest.qurio.presentation.settings

import com.bucharest.qurio.audio.AudioManager
import com.bucharest.qurio.domain.entity.Settings
import com.bucharest.qurio.domain.repository.SettingsRepository
import com.bucharest.qurio.presentation.base.BasePresenter

class SettingsPresenter(
    private val settingsRepository: SettingsRepository,
    private val audioManager: AudioManager
) : BasePresenter<SettingsView>() {

    private var currentSettings: Settings = Settings()
    private var originalSettings: Settings = Settings()

    override fun onViewAttached() {
        super.onViewAttached()
        loadSettings()
    }

    fun onSoundChanged(level: Int) {
        currentSettings = currentSettings.copy(soundLevel = level)
        audioManager.forceVolumeRefresh(level, currentSettings.musicLevel)
        audioManager.playTestSound()
        executeIfViewAttached {
            updateSoundLevel(level)
        }
    }

    fun onMusicChanged(level: Int) {
        currentSettings = currentSettings.copy(musicLevel = level)
        audioManager.forceVolumeRefresh(currentSettings.soundLevel, level)
        executeIfViewAttached {
            updateMusicLevel(level)
        }
    }

    fun onSaveClicked() {
        tryToExecute(
            execute = { 
                settingsRepository.saveSettings(currentSettings)
                audioManager.updateVolumeLevels()
            },
            onSuccess = {
                executeIfViewAttached {
                    showSavedMessage()
                    dismissDialog()
                }
            },
            onError = { throwable ->
                executeIfViewAttached {
                    showError(throwable.message ?: "Failed to save settings")
                }
            }
        )
    }

    fun onDiscardClicked() {
        currentSettings = originalSettings
        executeIfViewAttached {
            updateSoundLevel(originalSettings.soundLevel)
            updateMusicLevel(originalSettings.musicLevel)
            showDiscardedMessage()
            dismissDialog()
        }
    }

    fun getCurrentLevels(): Pair<Int, Int> {
        return Pair(currentSettings.soundLevel, currentSettings.musicLevel)
    }

    private fun loadSettings() {
        tryToExecute(
            execute = { settingsRepository.getSettings() },
            onSuccess = { settings ->
                currentSettings = settings
                originalSettings = settings
                executeIfViewAttached {
                    updateSoundLevel(settings.soundLevel)
                    updateMusicLevel(settings.musicLevel)
                }
            },
            onError = { throwable ->
                executeIfViewAttached {
                    showError(throwable.message ?: "Failed to load settings")
                }
            }
        )
    }
}