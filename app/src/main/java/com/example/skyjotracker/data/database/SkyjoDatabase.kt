package com.example.skyjotracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room

@Database(
        entities = [GameEntity::class, PlayerEntity::class, ScoreEntity::class],
        version = 1,
        exportSchema = false
)
abstract class SkyjoDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var Instance: SkyjoDatabase? = null

        fun getDatabase(context: Context): SkyjoDatabase{
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, SkyjoDatabase::class.java, "skyjo_database")
                        .build()
                        .also { Instance = it }
            }
        }
    }
}
