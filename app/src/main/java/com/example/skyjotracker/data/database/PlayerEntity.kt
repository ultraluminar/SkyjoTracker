package com.example.skyjotracker.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "players",
    foreignKeys =
        [ForeignKey(
            entity = GameEntity::class,
            parentColumns = ["gameId"],
            childColumns = ["gameId"],
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index("gameId")]
)
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true) val playerId: Long = 0,
    val gameId: Long,
    val name: String,
    val playerIndex: Int // To maintain the order 1, 2, 3...
)