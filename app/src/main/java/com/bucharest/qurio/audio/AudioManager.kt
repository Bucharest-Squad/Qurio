package com.bucharest.qurio.audio

import android.content.Context
import android.media.MediaPlayer
import android.media.AudioAttributes
import android.media.AudioManager as SystemAudioManager
import com.bucharest.qurio.R
import com.bucharest.qurio.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioManager @Inject constructor(
    private val context: Context,
    private val settingsRepository: SettingsRepository
) {
    
    private val systemAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as SystemAudioManager
    
    private var backgroundMusicPlayer: MediaPlayer? = null
    private val soundEffectPool = mutableListOf<MediaPlayer>()
    private var lastTestSoundTime = 0L
    
    private var isBackgroundMusicEnabled = true
    private var isSoundEffectsEnabled = true
    private var currentSoundVolume = 1.0f
    private var currentMusicVolume = 1.0f
    
    companion object {
        private const val TEST_SOUND_COOLDOWN = 200L
        private const val MAX_SOUND_EFFECTS = 5
        private const val MAX_VOLUME = 1.0f
        private const val MIN_VOLUME = 0.0f
    }
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        CoroutineScope(Dispatchers.IO).launch {
            val settings = settingsRepository.getSettings()
            isBackgroundMusicEnabled = settings.musicLevel > 0
            isSoundEffectsEnabled = true
            
            currentSoundVolume = (settings.soundLevel / 100f).coerceIn(MIN_VOLUME, MAX_VOLUME)
            currentMusicVolume = (settings.musicLevel / 100f).coerceIn(MIN_VOLUME, MAX_VOLUME)
        }
    }
    
    fun playButtonPress() {
        if (isSoundEffectsEnabled) {
            playSoundEffect(R.raw.button_press)
        }
    }
    
    fun playTestSound() {
        if (isSoundEffectsEnabled) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTestSoundTime > TEST_SOUND_COOLDOWN) {
                lastTestSoundTime = currentTime
                playSoundEffect(R.raw.button_press)
            }
        }
    }
    
    fun forceVolumeRefresh(soundLevel: Int, musicLevel: Int) {
        updateVolumeLevels(soundLevel, musicLevel)
        
        if (musicLevel > 0 && (backgroundMusicPlayer == null || !backgroundMusicPlayer!!.isPlaying)) {
            startBackgroundMusic()
        }
    }
    
    fun playCorrectAnswer() {
        if (isSoundEffectsEnabled) {
            playSoundEffect(R.raw.correct_answer)
        }
    }
    
    fun playWrongAnswer() {
        if (isSoundEffectsEnabled) {
            playSoundEffect(R.raw.wrong_answer)
        }
    }
    
    fun playGameWin() {
        if (isSoundEffectsEnabled) {
            playSoundEffect(R.raw.game_win)
        }
    }
    
    fun playGameOver() {
        if (isSoundEffectsEnabled) {
            playSoundEffect(R.raw.game_over)
        }
    }
    
    fun playCharacterSelect() {
        if (isSoundEffectsEnabled) {
            playSoundEffect(R.raw.select_character)
        }
    }
    
    fun playTimerTick() {
        if (isSoundEffectsEnabled) {
            try {
                playSoundEffect(R.raw.timer)
            } catch (e: Exception) {
                try {
                    playSoundEffect(R.raw.button_press)
                } catch (e2: Exception) {
                    playSoundEffect(R.raw.correct_answer)
                }
            }
        }
    }
    
    fun stopTimerSound() {
        soundEffectPool.forEach { player ->
            try {
                if (player.isPlaying) {
                    player.stop()
                }
                player.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        soundEffectPool.clear()
    }
    
    fun cleanupSoundEffects() {
        soundEffectPool.forEach { player ->
            try {
                if (player.isPlaying) {
                    player.stop()
                }
                player.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        soundEffectPool.clear()
    }
    
    private fun cleanupOldSoundEffects() {
        val iterator = soundEffectPool.iterator()
        while (iterator.hasNext()) {
            val player = iterator.next()
            try {
                if (!player.isPlaying) {
                    player.release()
                    iterator.remove()
                }
            } catch (e: Exception) {
                iterator.remove()
            }
        }
        
        if (soundEffectPool.size > MAX_SOUND_EFFECTS) {
            val toRemove = soundEffectPool.size - MAX_SOUND_EFFECTS
            for (i in 0 until toRemove) {
                try {
                    soundEffectPool[i].stop()
                    soundEffectPool[i].release()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            soundEffectPool.removeAll(soundEffectPool.take(toRemove))
        }
    }
    
    fun startBackgroundMusic() {
        if (isBackgroundMusicEnabled) {
            stopBackgroundMusic()
            backgroundMusicPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                setDataSource(context.resources.openRawResourceFd(R.raw.music))
                isLooping = true
                prepareAsync()
                setOnPreparedListener { 
                    try {
                        setVolume(currentMusicVolume, currentMusicVolume)
                        start() 
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
    
    fun stopBackgroundMusic() {
        backgroundMusicPlayer?.let { player ->
            if (player.isPlaying) {
                player.stop()
            }
            player.release()
        }
        backgroundMusicPlayer = null
    }
    
    fun pauseBackgroundMusic() {
        backgroundMusicPlayer?.pause()
    }
    
    fun resumeBackgroundMusic() {
        if (isBackgroundMusicEnabled) {
            backgroundMusicPlayer?.start()
        }
    }
    
    private fun playSoundEffect(resourceId: Int) {
        if (!isSoundEffectsEnabled) return
        
        try {
            cleanupOldSoundEffects()
            
            val player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                        .build()
                )
                setDataSource(context.resources.openRawResourceFd(resourceId))
                prepare()
                setVolume(currentSoundVolume, currentSoundVolume)
                start()
                
                setOnCompletionListener { 
                    try {
                        soundEffectPool.remove(this)
                        release()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                setOnErrorListener { _, _, _ ->
                    try {
                        soundEffectPool.remove(this)
                        release()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    true
                }
            }
            soundEffectPool.add(player)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun updateVolumeLevels() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val settings = settingsRepository.getSettings()
                isBackgroundMusicEnabled = settings.musicLevel > 0
                isSoundEffectsEnabled = true
                
                currentSoundVolume = (settings.soundLevel / 100f).coerceIn(MIN_VOLUME, MAX_VOLUME)
                currentMusicVolume = (settings.musicLevel / 100f).coerceIn(MIN_VOLUME, MAX_VOLUME)
                
                backgroundMusicPlayer?.let { player ->
                    try {
                        if (player.isPlaying) {
                            player.setVolume(currentMusicVolume, currentMusicVolume)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                
                if (!isBackgroundMusicEnabled) {
                    stopBackgroundMusic()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun updateVolumeLevels(soundLevel: Int, musicLevel: Int) {
        try {
            isBackgroundMusicEnabled = musicLevel > 0
            isSoundEffectsEnabled = true
            
            currentSoundVolume = (soundLevel / 100f).coerceIn(MIN_VOLUME, MAX_VOLUME)
            currentMusicVolume = (musicLevel / 100f).coerceIn(MIN_VOLUME, MAX_VOLUME)
            
            backgroundMusicPlayer?.let { player ->
                try {
                    if (player.isPlaying) {
                        player.setVolume(currentMusicVolume, currentMusicVolume)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            
            if (!isBackgroundMusicEnabled) {
                stopBackgroundMusic()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun release() {
        stopBackgroundMusic()
        cleanupSoundEffects()
    }
    
    fun performMaintenance() {
        cleanupOldSoundEffects()
    }
}