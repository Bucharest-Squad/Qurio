package com.bucharest.qurio.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.bucharest.qurio.domain.entity.Settings
import com.bucharest.qurio.domain.repository.SettingsRepository
import javax.inject.Inject
import androidx.core.content.edit

class SettingsRepositoryImpl @Inject constructor(
    private val context: Context
) : SettingsRepository {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        SETTINGS_PREFS_NAME,
        Context.MODE_PRIVATE
    )

    override suspend fun getSettings(): Settings {
        return Settings(
            soundLevel = prefs.getInt(SOUND_LEVEL_KEY, DEFAULT_SOUND_LEVEL),
            musicLevel = prefs.getInt(MUSIC_LEVEL_KEY, DEFAULT_MUSIC_LEVEL)
        )
    }

    override suspend fun saveSettings(settings: Settings) {
        prefs.edit {
            putInt(SOUND_LEVEL_KEY, settings.soundLevel)
                .putInt(MUSIC_LEVEL_KEY, settings.musicLevel)
        }
    }

    override suspend fun updateSoundLevel(level: Int) {
        prefs.edit {
            putInt(SOUND_LEVEL_KEY, level)
        }
    }

    override suspend fun updateMusicLevel(level: Int) {
        prefs.edit {
            putInt(MUSIC_LEVEL_KEY, level)
        }
    }

    override suspend fun getMusicVolume(): Float {
        val settings = getSettings()
        return settings.musicLevel / 100f
    }

    override suspend fun getEffectsVolume(): Float {
        val settings = getSettings()
        return settings.soundLevel / 100f
    }

    override suspend fun updateVolume(musicVolume: Float, effectsVolume: Float) {
        val musicLevel = (musicVolume * 100).toInt().coerceIn(0, 100)
        val soundLevel = (effectsVolume * 100).toInt().coerceIn(0, 100)
        
        prefs.edit {
            putInt(MUSIC_LEVEL_KEY, musicLevel)
                .putInt(SOUND_LEVEL_KEY, soundLevel)
        }
    }

    companion object {
        private const val SETTINGS_PREFS_NAME = "qurio_settings"
        private const val SOUND_LEVEL_KEY = "sound_level"
        private const val MUSIC_LEVEL_KEY = "music_level"
        private const val DEFAULT_SOUND_LEVEL = 70
        private const val DEFAULT_MUSIC_LEVEL = 50
    }
}