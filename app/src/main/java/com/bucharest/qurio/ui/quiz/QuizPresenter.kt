package com.bucharest.qurio.ui.quiz

import com.bucharest.qurio.base.BasePresenter
import com.bucharest.qurio.domain.repository.TriviaRepository
import javax.inject.Inject

class QuizPresenter @Inject constructor(
    private val triviaRepository: TriviaRepository
) : BasePresenter<QuizView>() {

    fun loadQuestions(amount: Int = 10) {
        tryToExecute(
            execute = {
                triviaRepository.getQuestions(amount = amount)
            },
            onSuccess = { result ->
                result.fold(
                    onSuccess = { questions ->
                        executeIfViewAttached {
                            if (questions.isNotEmpty()) {
                                displayQuestions(questions)
                            } else {
                                showEmptyState()
                            }
                        }
                    },
                    onFailure = { error ->
                        executeIfViewAttached {
                            showError(error.message ?: "Failed to load questions")
                        }
                    }
                )
            },
            onError = { throwable ->
                executeIfViewAttached {
                    showError(throwable.message ?: "An error occurred")
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