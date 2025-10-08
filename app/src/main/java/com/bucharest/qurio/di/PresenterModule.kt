package com.bucharest.qurio.di

import com.bucharest.qurio.domain.repository.TriviaRepository
import com.bucharest.qurio.ui.home.HomePresenter
import com.bucharest.qurio.ui.quiz.QuizPresenter
import dagger.Module
import dagger.Provides

@Module
class PresenterModule {

    @Provides
    fun provideHomePresenter(): HomePresenter {
        return HomePresenter()
    }

    @Provides
    fun provideQuizPresenter(
        triviaRepository: TriviaRepository
    ): QuizPresenter {
        return QuizPresenter(triviaRepository)
    }
}