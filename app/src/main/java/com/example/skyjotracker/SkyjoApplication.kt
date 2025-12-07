package com.example.skyjotracker

import android.app.Application
import com.example.skyjotracker.data.GameRepository
import com.example.skyjotracker.data.database.SkyjoDatabase

class SkyjoApplication : Application() {

    lateinit var database: SkyjoDatabase
    lateinit var repository: GameRepository

    override fun onCreate() {
        super.onCreate()
        database = SkyjoDatabase.getDatabase(applicationContext)
        repository = GameRepository(database.gameDao())
    }
}
