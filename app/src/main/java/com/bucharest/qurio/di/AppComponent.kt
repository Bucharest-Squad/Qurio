package com.bucharest.qurio.di

import android.app.Application
import com.bucharest.qurio.audio.AudioManager
import com.bucharest.qurio.data.local.AppDatabase
import com.bucharest.qurio.MainActivity
import com.bucharest.qurio.presentation.home.MainHomeFragment
import com.bucharest.qurio.presentation.game.GameFragment
import com.bucharest.qurio.presentation.result.ResultFragment
import com.bucharest.qurio.presentation.difficulty.DifficultyLevelFragment
import com.bucharest.qurio.presentation.lastgames.LastGamesFragment
import com.bucharest.qurio.presentation.games.GamesFragment
import com.bucharest.qurio.presentation.onboarding.OnBoardingFragment
import com.bucharest.qurio.presentation.settings.SettingsDialog
import com.bucharest.qurio.presentation.component.CharactersDialog
import com.bucharest.qurio.presentation.component.CharacterPurchaseDialog
import com.bucharest.qurio.presentation.component.CharacterDetailsDialog
import com.bucharest.qurio.presentation.achievemetns_dialog.AchievementsDialog
import com.bucharest.qurio.presentation.achievemetns_dialog.AchievementDetailsDialog
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        DatabaseModule::class,
        RepositoryModule::class,
        PresenterModule::class,
        AudioModule::class
    ]
)
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(fragment: MainHomeFragment)
    fun inject(fragment: GameFragment)
    fun inject(fragment: ResultFragment)
    fun inject(fragment: DifficultyLevelFragment)
    fun inject(fragment: LastGamesFragment)
    fun inject(fragment: GamesFragment)
    fun inject(fragment: OnBoardingFragment)
    fun inject(dialog: SettingsDialog)
    fun inject(dialog: CharactersDialog)
    fun inject(dialog: CharacterPurchaseDialog)
    fun inject(dialog: CharacterDetailsDialog)
    fun inject(dialog: AchievementsDialog)
    fun inject(dialog: AchievementDetailsDialog)

    fun getDatabase(): AppDatabase
    fun getSettingsRepository(): com.bucharest.qurio.domain.repository.SettingsRepository
    fun getAudioManager(): AudioManager

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}