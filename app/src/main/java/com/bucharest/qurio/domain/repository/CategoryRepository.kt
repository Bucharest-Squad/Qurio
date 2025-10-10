package com.bucharest.qurio.domain.repository

import com.bucharest.qurio.domain.entity.Category

interface CategoryRepository {
    suspend fun getAllCategories(): List<Category>
    suspend fun getCategoryById(id: Int): Category?
}


