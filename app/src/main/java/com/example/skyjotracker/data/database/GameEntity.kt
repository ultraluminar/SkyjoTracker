package com.example.skyjotracker.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true) val gameId: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val isFinished: Boolean = false,
    val imageUri: String? = null
)
