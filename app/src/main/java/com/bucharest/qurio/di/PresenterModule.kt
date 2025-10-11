package com.bucharest.qurio.di

import android.app.Application
import com.bucharest.qurio.domain.repository.CategoryRepository
import com.bucharest.qurio.domain.repository.GameRepository
import com.bucharest.qurio.domain.repository.TriviaRepository
import com.bucharest.qurio.domain.repository.UserRepository
import com.bucharest.qurio.presentation.home.MainHomePresenter
import com.bucharest.qurio.presentation.quiz.QuizPresenter
import dagger.Module
import dagger.Provides

@Module
object PresenterModule {

    @Provides
    fun provideMainHomePresenter(
        userRepository: UserRepository,
        gameRepository: GameRepository,
        categoryRepository: CategoryRepository,
        context: Application
    ): MainHomePresenter = MainHomePresenter(userRepository, gameRepository, categoryRepository, context)

    @Provides
    fun provideQuizPresenter(triviaRepository: TriviaRepository): QuizPresenter =
        QuizPresenter(triviaRepository)
}