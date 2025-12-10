package com.example.skyjotracker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.skyjotracker.SkyjoApplication
import com.example.skyjotracker.data.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

data class SettingsUiState(
    val currentLocale: Locale = Locale.getDefault()
)

class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = userPreferencesRepository.language
        .map { languageCode ->
            if (languageCode != null) {
                SettingsUiState(Locale.forLanguageTag(languageCode))
            } else {
                SettingsUiState(Locale.getDefault())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState()
        )

    fun setLanguage(languageCode: String) {
        viewModelScope.launch {
            userPreferencesRepository.setLanguage(languageCode)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SkyjoApplication)
                SettingsViewModel(application.userPreferencesRepository)
            }
        }
    }
}
