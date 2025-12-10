package com.example.skyjotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.skyjotracker.ui.LocaleContextWrapper
import com.example.skyjotracker.ui.settings.SettingsViewModel
import com.example.skyjotracker.ui.theme.SkyjoTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as SkyjoApplication
        val settingsViewModel = SettingsViewModel(app.userPreferencesRepository)

        setContent {
            val settingsUiState by settingsViewModel.uiState.collectAsState()

            SkyjoTrackerTheme {
                LocaleContextWrapper(settingsUiState.currentLocale) {
                    SkyjoTrackerApp(settingsViewModel = settingsViewModel)
                }
            }
        }
    }
}
