package com.example.skyjotracker.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.skyjotracker.SkyjoApplication
import com.example.skyjotracker.ui.game.GameViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory { initializer { GameViewModel(skyjoApplication().repository) } }
}

fun CreationExtras.skyjoApplication(): SkyjoApplication =
        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SkyjoApplication)