package com.example.skyjotracker.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "scores",
    foreignKeys =
        [
            ForeignKey(
                entity = GameEntity::class,
                parentColumns = ["gameId"],
                childColumns = ["gameId"],
                onDelete = ForeignKey.CASCADE
            ),
            ForeignKey(
                entity = PlayerEntity::class,
                parentColumns = ["playerId"],
                childColumns = ["playerId"],
                onDelete = ForeignKey.CASCADE
            )
        ],
    indices = [Index("gameId"), Index("playerId")]
)
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true) val scoreId: Long = 0,
    val gameId: Long,
    val playerId: Long,
    val roundNumber: Int,
    val score: Int
)