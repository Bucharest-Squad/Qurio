package com.bucharest.qurio.data.repository

import com.bucharest.qurio.data.local.dao.CategoryDao
import com.bucharest.qurio.data.mapper.toEntity
import com.bucharest.qurio.domain.entity.Category
import com.bucharest.qurio.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override suspend fun getAllCategories(): List<Category> =
        categoryDao.getAll().map { it.toEntity() }

    override suspend fun getCategoryById(id: Int): Category? =
        categoryDao.getById(id)?.toEntity()
}


