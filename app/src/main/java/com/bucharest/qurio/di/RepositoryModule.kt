package com.bucharest.qurio.di

import com.bucharest.qurio.data.repository.TriviaRepositoryImpl
import com.bucharest.qurio.data.repository.UserRepositoryImpl
import com.bucharest.qurio.data.repository.CharacterRepositoryImpl
import com.bucharest.qurio.data.repository.AchievementRepositoryImpl
import com.bucharest.qurio.data.repository.CategoryRepositoryImpl
import com.bucharest.qurio.data.repository.GameRepositoryImpl
import com.bucharest.qurio.domain.repository.TriviaRepository
import com.bucharest.qurio.domain.repository.UserRepository
import com.bucharest.qurio.domain.repository.CharacterRepository
import com.bucharest.qurio.domain.repository.AchievementRepository
import com.bucharest.qurio.domain.repository.CategoryRepository
import com.bucharest.qurio.domain.repository.GameRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTriviaRepository(
        triviaRepositoryImpl: TriviaRepositoryImpl
    ): TriviaRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindCharacterRepository(
        impl: CharacterRepositoryImpl
    ): CharacterRepository

    @Binds
    @Singleton
    abstract fun bindAchievementRepository(
        impl: AchievementRepositoryImpl
    ): AchievementRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindGameRepository(
        impl: GameRepositoryImpl
    ): GameRepository
}