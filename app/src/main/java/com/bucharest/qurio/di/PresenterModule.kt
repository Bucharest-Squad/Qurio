package com.bucharest.qurio.di

import com.bucharest.qurio.domain.repository.TriviaRepository
import com.bucharest.qurio.presentation.home.HomePresenter
import com.bucharest.qurio.presentation.quiz.QuizPresenter
import dagger.Module
import dagger.Provides

@Module
object PresenterModule {

    @Provides
    fun provideHomePresenter(): HomePresenter = HomePresenter()

    @Provides
    fun provideQuizPresenter(triviaRepository: TriviaRepository): QuizPresenter =
        QuizPresenter(triviaRepository)
}