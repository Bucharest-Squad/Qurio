package com.bucharest.qurio.data.seed

import com.bucharest.qurio.data.local.AppDatabase

class DataSeeder(
    private val database: AppDatabase,
    private val seedDataProvider: SeedDataProvider
) {
    suspend fun seedIfEmpty() {
        if (database.characterDao().getAll().isEmpty()) {
            database.characterDao().insertAll(seedDataProvider.characters())
        }
        if (database.achievementDao().getAll().isEmpty()) {
            database.achievementDao().insertAll(seedDataProvider.achievements())
        }
        if (database.categoryDao().getAll().isEmpty()) {
            database.categoryDao().insertAll(seedDataProvider.categories())
        }
    }
}