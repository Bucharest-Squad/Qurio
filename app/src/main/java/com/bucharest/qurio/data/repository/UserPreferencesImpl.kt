package com.bucharest.qurio.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import com.bucharest.qurio.domain.repository.UserPreferences
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.map

class UserPreferencesImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferences {

    override val isFirstLaunch: Flow<Boolean>
        get() {
            return dataStore.data.map {
                it[FIRST_LAUNCH] ?: true
            }
        }

    override suspend fun setFirstLaunch() {
        dataStore.edit {
            it[FIRST_LAUNCH] = false
        }
    }

    private companion object {
        val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
    }
}