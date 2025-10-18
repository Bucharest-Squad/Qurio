package com.bucharest.qurio.di

import android.content.Context
import com.bucharest.qurio.audio.AudioManager
import com.bucharest.qurio.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AudioModule {

    @Provides
    @Singleton
    fun provideAudioManager(
        context: Context,
        settingsRepository: SettingsRepository
    ): AudioManager {
        return AudioManager(context, settingsRepository)
    }
}