package com.example.skyjotracker.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {
    private object PreferencesKeys {
        val LANGUAGE_CODE = stringPreferencesKey("language_code")
    }

    val language: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.LANGUAGE_CODE]
    }

    suspend fun setLanguage(languageCode: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE_CODE] = languageCode
        }
    }
}
