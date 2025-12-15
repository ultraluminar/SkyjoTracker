package com.example.skyjotracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [GameEntity::class, PlayerEntity::class, ScoreEntity::class],
    version = 2,
    exportSchema = false
)
abstract class SkyjoDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var Instance: SkyjoDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE games ADD COLUMN imageUri TEXT")
            }
        }

        fun getDatabase(context: Context): SkyjoDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, SkyjoDatabase::class.java, "skyjo_database")
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
