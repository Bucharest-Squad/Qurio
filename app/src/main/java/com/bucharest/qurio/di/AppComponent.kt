package com.bucharest.qurio.di

import android.app.Application
import com.bucharest.qurio.presentation.home.HomeFragment
import com.bucharest.qurio.presentation.quiz.QuizFragment
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

    fun inject(fragment: HomeFragment)
    fun inject(fragment: QuizFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}