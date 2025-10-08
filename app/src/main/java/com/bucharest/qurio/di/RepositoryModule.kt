package com.bucharest.qurio.di

import com.bucharest.qurio.data.repository.TriviaRepositoryImpl
import com.bucharest.qurio.domain.repository.TriviaRepository
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
}