package com.bucharest.qurio.di

import android.app.Application
import com.bucharest.qurio.data.local.AppDatabase
import com.bucharest.qurio.presentation.home.MainHomeFragment
import com.bucharest.qurio.presentation.game.GameFragment
import com.bucharest.qurio.presentation.result.ResultFragment
import com.bucharest.qurio.presentation.difficulty.DifficultyLevelFragment
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
        PresenterModule::class
    ]
)
interface AppComponent {

    fun inject(fragment: MainHomeFragment)
    fun inject(fragment: GameFragment)
    fun inject(fragment: ResultFragment)
    fun inject(fragment: DifficultyLevelFragment)
    
    fun getDatabase(): AppDatabase

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}