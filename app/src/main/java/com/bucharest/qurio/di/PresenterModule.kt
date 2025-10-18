package com.bucharest.qurio.di

import android.app.Application
import com.bucharest.qurio.domain.repository.AchievementRepository
import com.bucharest.qurio.domain.repository.CategoryRepository
import com.bucharest.qurio.domain.repository.CharacterRepository
import com.bucharest.qurio.domain.repository.GameRepository
import com.bucharest.qurio.domain.repository.UserRepository
import com.bucharest.qurio.presentation.home.MainHomePresenter
import com.bucharest.qurio.presentation.game.GamePresenter
import com.bucharest.qurio.presentation.result.ResultPresenter
import com.bucharest.qurio.presentation.difficulty.DifficultyLevelPresenter
import com.bucharest.qurio.presentation.lastgames.LastGamesPresenter
import com.bucharest.qurio.presentation.buy_life.BuyLifePresenter
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
    ): MainHomePresenter = MainHomePresenter(
        userRepository,
        gameRepository,
        characterRepository,
        achievementRepository,
        categoryRepository,
        context
    )

    @Provides
    fun provideGamePresenter(
        gameRepository: GameRepository,
        userRepository: UserRepository
    ): GamePresenter = GamePresenter(gameRepository, userRepository)

    @Provides
    fun provideResultPresenter(): ResultPresenter = ResultPresenter()

    @Provides
    fun provideDifficultyLevelPresenter(): DifficultyLevelPresenter = DifficultyLevelPresenter()

    @Provides
    fun provideLastGamesPresenter(
        gameRepository: GameRepository,
        context: Application
    ): LastGamesPresenter = LastGamesPresenter(gameRepository, context)

    @Provides
    fun provideBuyLifePresenter(
        userRepository: UserRepository
    ): BuyLifePresenter = BuyLifePresenter(userRepository)
}