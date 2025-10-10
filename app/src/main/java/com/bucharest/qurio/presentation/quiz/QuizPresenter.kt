package com.bucharest.qurio.presentation.quiz

import com.bucharest.qurio.domain.model.QuestionFilter
import com.bucharest.qurio.presentation.base.BasePresenter
import com.bucharest.qurio.domain.repository.TriviaRepository
import javax.inject.Inject

class QuizPresenter @Inject constructor(
    private val triviaRepository: TriviaRepository
) : BasePresenter<QuizView>() {

    fun loadQuestions(amount: Int = 10) {
        tryToExecute(
            execute = {
                triviaRepository.getQuestions(filter = QuestionFilter(amount = amount))
            },
            onSuccess = { questions ->
                executeIfViewAttached {
                    if (questions.isNotEmpty()) {
                        displayQuestions(questions)
                    } else {
                        showEmptyState()
                    }
                }
            },
            onError = { throwable ->
                executeIfViewAttached {
                    showError(throwable.message ?: "Failed to load questions")
                }
            },
            onStart = {
                executeIfViewAttached {
                    showLoading()
                }
            },
            onFinally = {
                executeIfViewAttached {
                    hideLoading()
                }
            }
        )
    }
}