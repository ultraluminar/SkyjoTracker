package com.example.skyjotracker.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skyjotracker.data.GameRepository
import com.example.skyjotracker.data.GameUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(private val repository: GameRepository) : ViewModel() {

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
