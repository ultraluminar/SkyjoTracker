package com.example.skyjotracker.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertGame(game: GameEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: PlayerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(score: ScoreEntity): Long

    @Query("SELECT * FROM games ORDER BY timestamp DESC")
    fun getAllGames(): kotlinx.coroutines.flow.Flow<List<GameEntity>>

    @Query("SELECT * FROM games WHERE gameId = :gameId")
    suspend fun getGame(gameId: Long): GameEntity?

    @Query("SELECT * FROM players WHERE gameId = :gameId ORDER BY playerIndex")
    suspend fun getPlayersForGame(gameId: Long): List<PlayerEntity>

    @Query("SELECT * FROM scores WHERE gameId = :gameId")
    suspend fun getScoresForGame(gameId: Long): List<ScoreEntity>

    // Helper to get scores for a specific game and player handy for some logic if needed
    @Query("SELECT * FROM scores WHERE gameId = :gameId AND playerId = :playerId")
    suspend fun getScoresForPlayer(gameId: Long, playerId: Long): List<ScoreEntity>
}
