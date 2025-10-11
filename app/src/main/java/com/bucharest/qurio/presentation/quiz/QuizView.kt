package com.bucharest.qurio.presentation.quiz

import com.bucharest.qurio.presentation.base.BaseView
import com.bucharest.qurio.domain.entity.Question

interface QuizView : BaseView {
    fun displayQuestions(questions: List<Question>)
    fun showEmptyState()
}