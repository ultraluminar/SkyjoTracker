package com.example.skyjotracker.ui.game

import androidx.lifecycle.ViewModel
import com.example.skyjotracker.data.GameUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun setPlayerNames(inputPlayerNames: List<String>) {
        val playerNames = inputPlayerNames.filter { it.isNotEmpty() }
            .mapIndexed { index, name -> index + 1 to name }
            .toMap()
        _uiState.update { currentState ->
            currentState.copy(
                playerNames = playerNames,
                currentRound = 1,
                totalScores = playerNames.mapValues { 0 }
            )
        }
    }

    fun setNumberOfRounds(inputNumberOfRounds: Int?) {
        _uiState.update { currentState ->
            currentState.copy(
                numberOfRounds = inputNumberOfRounds
            )
        }
    }

    fun setPlayerScores(inputPlayerScores: Map<Int, Int>) {
        val scoreSheet = _uiState.value.scoreSheet.toMutableMap()
        val currentRound = _uiState.value.currentRound
        scoreSheet[currentRound] = inputPlayerScores
        _uiState.update { currentState ->
            currentState.copy(
                currentRound = currentRound + 1,
                scoreSheet = scoreSheet
            )
        }
        setTotalScores()
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
    }

    private fun setTotalScores() {
        val scoreSheet = _uiState.value.scoreSheet
        val playerNames = _uiState.value.playerNames
        val totalScores = mutableMapOf<Int, Int>()
        playerNames.keys.forEach { playerId ->
            totalScores[playerId] = 0
        }
        scoreSheet.values.forEach { roundScores ->
            roundScores.forEach { (playerId, score) ->
                totalScores[playerId] = (totalScores[playerId] ?: 0) + score
            }
        }
        _uiState.update { currentState ->
            currentState.copy(
                totalScores = totalScores
            )
        }
    }
}