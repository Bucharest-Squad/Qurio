package com.bucharest.qurio.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferences {
    val isFirstLaunch: Flow<Boolean>
    suspend fun setFirstLaunch()
}
