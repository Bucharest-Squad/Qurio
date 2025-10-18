package com.bucharest.qurio

import android.media.AudioManager as SystemAudioManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.bucharest.qurio.audio.AudioManager
import com.bucharest.qurio.databinding.ActivityMainBinding
import com.bucharest.qurio.domain.repository.UserPreferences
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var audioManager: AudioManager
    
    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as QurioApp).appComponent.inject(this)
        val splashScreen = installSplashScreen()
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            splashScreenView.remove()
        }
        
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.hide()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setupActionBarWithNavController(navController)
        
        // Check first launch and navigate accordingly
        checkFirstLaunchAndNavigate()
        
        // Audio management
        disableSystemSoundEffects()
        
        audioManager = (application as QurioApp).appComponent.getAudioManager()
        audioManager.startBackgroundMusic()
    }
    
    private fun checkFirstLaunchAndNavigate() {
        lifecycleScope.launch {
            userPreferences.isFirstLaunch.collect { isFirstLaunch ->
                if (!isFirstLaunch) {
                    // Not first launch, navigate to home
                    val navController = (supportFragmentManager
                        .findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
                    navController.navigate(R.id.mainHomeFragment)
                }
                // If isFirstLaunch is true, stay on onboarding (default start destination)
            }
        }
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
        window.decorView.isSoundEffectsEnabled = false
    }
    
    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()
    }
}