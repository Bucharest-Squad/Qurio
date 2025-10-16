package com.bucharest.qurio.di

import android.app.Application
import com.bucharest.qurio.domain.repository.AchievementRepository
import com.bucharest.qurio.domain.repository.CategoryRepository
import com.bucharest.qurio.domain.repository.CharacterRepository
import com.bucharest.qurio.domain.repository.GameRepository
import com.bucharest.qurio.domain.repository.UserRepository
import com.bucharest.qurio.presentation.home.MainHomePresenter
import dagger.Module
import dagger.Provides

@Module
object PresenterModule {

    @Provides
    fun provideMainHomePresenter(
        userRepository: UserRepository,
        gameRepository: GameRepository,
        categoryRepository: CategoryRepository,
        characterRepository: CharacterRepository,
        achievementRepository: AchievementRepository,
        context: Application
    ): MainHomePresenter = MainHomePresenter(userRepository, gameRepository, ,characterRepository,achievementRepository,categoryRepository, context)
}