package com.bucharest.qurio.domain.repository

import com.bucharest.qurio.domain.entity.Question
import com.bucharest.qurio.domain.model.QuestionFilter

interface TriviaRepository {
    suspend fun getQuestions(filter: QuestionFilter = QuestionFilter()): List<Question>
}