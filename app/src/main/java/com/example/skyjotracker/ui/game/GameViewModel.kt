package com.example.skyjotracker.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skyjotracker.data.GameRepository
import com.example.skyjotracker.data.GameUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(private val repository: GameRepository) : ViewModel() {

    private val _navigationEvent = Channel<GameScreen>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var currentGameId: Long? = null
    private val playerDbIds = mutableMapOf<Int, Long>()

    fun setPlayerNames(inputPlayerNames: List<String>) {
        val playerNames =
            inputPlayerNames
                .filter { it.isNotEmpty() }
                .mapIndexed { index, name -> index + 1 to name }
                .toMap()

        _uiState.update { currentState ->
            currentState.copy(
                playerNames = playerNames,
                currentRound = 1,
                totalScores = playerNames.mapValues { 0 }
            )
        }

        viewModelScope.launch {
            val gameId = repository.createGame()
            currentGameId = gameId
            playerDbIds.clear()
            playerNames.forEach { (index, name) ->
                val playerId = repository.savePlayer(gameId, name, index)
                playerDbIds[index] = playerId
            }
        }
    }

    fun setNumberOfRounds(inputNumberOfRounds: Int?) {
        _uiState.update { currentState -> currentState.copy(numberOfRounds = inputNumberOfRounds) }
    }

    fun setPlayerScores(inputPlayerScores: Map<Int, Int>) {
        val scoreSheet = _uiState.value.scoreSheet.toMutableMap()
        val currentRound = _uiState.value.currentRound
        scoreSheet[currentRound] = inputPlayerScores
        _uiState.update { currentState ->
            currentState.copy(currentRound = currentRound + 1, scoreSheet = scoreSheet)
        }
        setTotalScores()

        viewModelScope.launch {
            currentGameId?.let { gameId ->
                inputPlayerScores.forEach { (playerIndex, score) ->
                    val dbPlayerId = playerDbIds[playerIndex]
                    if (dbPlayerId != null) {
                        repository.saveScore(gameId, dbPlayerId, currentRound, score)
                    }
                }
                // if the current round is the last round, set the game as finished
                if (_uiState.value.numberOfRounds != null &&
                    _uiState.value.numberOfRounds == currentRound
                ) {
                    setGameFinished()
                }
            }
        }
    }

    fun reset() {
        _uiState.update { currentState ->
            currentState.copy(
                playerNames = mapOf(),
                currentRound = 1,
                scoreSheet = mapOf(),
                totalScores = mapOf(),
                numberOfRounds = null
            )
        }
        currentGameId = null
        playerDbIds.clear()
    }

    fun setGameFinished() {
        viewModelScope.launch {
            currentGameId?.let { gameId -> repository.setGameFinished(gameId) }
        }
    }

    private fun setTotalScores() {
        val scoreSheet = _uiState.value.scoreSheet
        val playerNames = _uiState.value.playerNames
        val totalScores = mutableMapOf<Int, Int>()
        playerNames.keys.forEach { playerId -> totalScores[playerId] = 0 }
        scoreSheet.values.forEach { roundScores ->
            roundScores.forEach { (playerId, score) ->
                totalScores[playerId] = (totalScores[playerId] ?: 0) + score
            }
        }
        _uiState.update { currentState -> currentState.copy(totalScores = totalScores) }
    }

    fun loadGame(gameId: Long) {
        viewModelScope.launch {
            val game = repository.getGame(gameId) ?: return@launch
            val players = repository.getPlayers(gameId)
            val scores = repository.getScores(gameId)

            // Reconstruct players map
            val playerNames = players.associate { it.playerIndex to it.name }
            playerDbIds.clear()
            players.forEach { playerDbIds[it.playerIndex] = it.playerId }

            // Reconstruct score sheet
            val scoreSheet = mutableMapOf<Int, MutableMap<Int, Int>>()
            scores.forEach { scoreEntity ->
                val roundScores = scoreSheet.getOrPut(scoreEntity.roundNumber) { mutableMapOf() }
                // Map db player id back to player index
                val playerIndex = players.find { it.playerId == scoreEntity.playerId }?.playerIndex
                if (playerIndex != null) {
                    roundScores[playerIndex] = scoreEntity.score
                }
            }

            // Determine current round (max round + 1)
            val maxRoundNode = scoreSheet.keys.maxOrNull() ?: 0
            val currentRound = maxRoundNode + 1

            currentGameId = gameId

            // Update UI State
            _uiState.update {
                it.copy(
                    playerNames = playerNames,
                    // If game is finished, we might want to stay on the last round or go to
                    // finish screen
                    // Logic here assumes resuming for play usually.
                    // However, if finished, we just want to view data.
                    // Let caller handle navigation. Here we just set state.
                    currentRound = currentRound,
                    scoreSheet = scoreSheet,
                    numberOfRounds = null, // Not persisted currently, so null.
                    totalScores = calculateTotalScores(scoreSheet, playerNames.keys)
                )
            }

            if (game.isFinished) {
                _navigationEvent.send(GameScreen.FINISH)
            } else {
                _navigationEvent.send(GameScreen.ROUND)
            }
        }
    }

    private fun calculateTotalScores(
        scoreSheet: Map<Int, Map<Int, Int>>,
        playerIds: Set<Int>
    ): Map<Int, Int> {
        val totalScores = mutableMapOf<Int, Int>()
        playerIds.forEach { playerId -> totalScores[playerId] = 0 }
        scoreSheet.values.forEach { roundScores ->
            roundScores.forEach { (playerId, score) ->
                totalScores[playerId] = (totalScores[playerId] ?: 0) + score
            }
        }
        return totalScores
    }

    class Factory(private val repository: GameRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
                return GameViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
