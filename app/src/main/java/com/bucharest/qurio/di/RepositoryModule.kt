package com.bucharest.qurio.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.bucharest.qurio.data.repository.AchievementRepositoryImpl
import com.bucharest.qurio.data.repository.CategoryRepositoryImpl
import com.bucharest.qurio.data.repository.CharacterRepositoryImpl
import com.bucharest.qurio.data.repository.GameRepositoryImpl
import com.bucharest.qurio.data.repository.TriviaRepositoryImpl
import com.bucharest.qurio.data.repository.UserPreferencesImpl
import com.bucharest.qurio.data.repository.UserRepositoryImpl
import com.bucharest.qurio.domain.repository.AchievementRepository
import com.bucharest.qurio.domain.repository.CategoryRepository
import com.bucharest.qurio.domain.repository.CharacterRepository
import com.bucharest.qurio.domain.repository.GameRepository
import com.bucharest.qurio.domain.repository.TriviaRepository
import com.bucharest.qurio.domain.repository.UserPreferences
import com.bucharest.qurio.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
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

    @Binds
    @Singleton
    abstract fun bindUserPreferences(
        impl: UserPreferencesImpl
    ): UserPreferences
}