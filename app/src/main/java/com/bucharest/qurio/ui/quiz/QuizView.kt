package com.bucharest.qurio.ui.quiz

import com.bucharest.qurio.base.BaseView
import com.bucharest.qurio.domain.entity.Question

interface QuizView : BaseView {
    fun displayQuestions(questions: List<Question>)
    fun showEmptyState()
}