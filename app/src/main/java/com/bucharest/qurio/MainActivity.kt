package com.bucharest.qurio

import android.media.AudioManager as SystemAudioManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.bucharest.qurio.audio.AudioManager
import com.bucharest.qurio.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var audioManager: AudioManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        // Dismiss splash screen immediately without waiting for data
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            splashScreenView.remove()
        }
        
        // Configure system bars to be transparent
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.hide()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setupActionBarWithNavController(navController)
        
        disableSystemSoundEffects()
        
        audioManager = (application as QurioApp).appComponent.getAudioManager()
        audioManager.startBackgroundMusic()
    }
    
    override fun onResume() {
        super.onResume()
        audioManager.resumeBackgroundMusic()
        audioManager.performMaintenance()
    }
    
    override fun onPause() {
        super.onPause()
        audioManager.pauseBackgroundMusic()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        audioManager.release()
    }

    private fun disableSystemSoundEffects() {
        val systemAudioManager = getSystemService(AUDIO_SERVICE) as SystemAudioManager
        systemAudioManager.setStreamMute(SystemAudioManager.STREAM_SYSTEM, true)
        window.decorView.setSoundEffectsEnabled(false)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()
    }

}
