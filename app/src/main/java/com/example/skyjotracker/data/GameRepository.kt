package com.example.skyjotracker.data

import com.example.skyjotracker.data.database.GameDao
import com.example.skyjotracker.data.database.GameEntity
import com.example.skyjotracker.data.database.PlayerEntity
import com.example.skyjotracker.data.database.ScoreEntity

class GameRepository(private val gameDao: GameDao) {

    fun getAllGamesStream() = gameDao.getAllGames()

    suspend fun createGame(): Long {
        return gameDao.insertGame(GameEntity())
    }

    suspend fun savePlayer(gameId: Long, name: String, index: Int): Long {
        val player = PlayerEntity(gameId = gameId, name = name, playerIndex = index)
        return gameDao.insertPlayer(player)
    }

    suspend fun saveScore(gameId: Long, playerId: Long, roundNumber: Int, scoreValue: Int) {
        val score =
                ScoreEntity(
                        gameId = gameId,
                        playerId = playerId,
                        roundNumber = roundNumber,
                        score = scoreValue
                )
        gameDao.insertScore(score)
    }

    suspend fun setGameFinished(gameId: Long) {
        gameDao.setGameFinished(gameId)
    }
}
