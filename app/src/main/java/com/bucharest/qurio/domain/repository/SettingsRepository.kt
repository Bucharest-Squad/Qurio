package com.bucharest.qurio.domain.repository

import com.bucharest.qurio.domain.entity.Settings

interface SettingsRepository {
    suspend fun getSettings(): Settings
    suspend fun saveSettings(settings: Settings)
    suspend fun updateSoundLevel(level: Int)
    suspend fun updateMusicLevel(level: Int)
    suspend fun getMusicVolume(): Float
    suspend fun getEffectsVolume(): Float
    suspend fun updateVolume(musicVolume: Float, effectsVolume: Float)
}