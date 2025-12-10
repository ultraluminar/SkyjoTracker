package com.example.skyjotracker

import android.app.Application
import com.example.skyjotracker.data.GameRepository
import com.example.skyjotracker.data.UserPreferencesRepository
import com.example.skyjotracker.data.database.SkyjoDatabase

class SkyjoApplication : Application() {

    lateinit var database: SkyjoDatabase
    lateinit var repository: GameRepository
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        database = SkyjoDatabase.getDatabase(applicationContext)
        repository = GameRepository(database.gameDao())
        userPreferencesRepository = UserPreferencesRepository(applicationContext)
    }
}
