package com.example.skyjotracker.game

import com.example.skyjotracker.ui.game.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.Int

class GameViewModelTest {

    private val viewModel = GameViewModel()

    @Test
    fun setPlayerNames_setsPlayerNamesAndInitialScores() {
        val playerNames = listOf("Player 1", "Player 2")
        viewModel.setPlayerNames(playerNames)

        val uiState = viewModel.uiState.value
        assertEquals(2, uiState.playerNames.size)
        assertEquals("Player 1", uiState.playerNames[1])
        assertEquals("Player 2", uiState.playerNames[2])
        assertEquals(1, uiState.currentRound)
        assertEquals(0, uiState.totalScores[1])
        assertEquals(0, uiState.totalScores[2])
    }

    @Test
    fun setNumberOfRounds_setsNumberOfRounds() {
        val numberOfRounds = 6
        viewModel.setNumberOfRounds(numberOfRounds)

        val uiState = viewModel.uiState.value
        assertEquals(numberOfRounds, uiState.numberOfRounds)
    }

    @Test
    fun setPlayerScores_updatesScoresAndRound() {
        val playerNames = listOf("Player 1", "Player 2")
        viewModel.setPlayerNames(playerNames)

        val scores = mapOf(1 to 10, 2 to 20)
        viewModel.setPlayerScores(scores)

        val uiState = viewModel.uiState.value
        assertEquals(2, uiState.currentRound)
        assertEquals(scores, uiState.scoreSheet[1])
        assertEquals(10, uiState.totalScores[1])
        assertEquals(20, uiState.totalScores[2])
    }

    @Test
    fun setTotalScores_calculatesTotalScores() {
        val playerNames = listOf("Player 1", "Player 2")
        viewModel.setPlayerNames(playerNames)

        val round1Scores = mapOf(1 to 10, 2 to 20)
        viewModel.setPlayerScores(round1Scores)

        val round2Scores = mapOf(1 to 5, 2 to 15)
        viewModel.setPlayerScores(round2Scores)

        val uiState = viewModel.uiState.value
        assertEquals(3, uiState.currentRound) // Round incremented twice
        assertEquals(15, uiState.totalScores[1])
        assertEquals(35, uiState.totalScores[2])
    }

    @Test
    fun reset_resetsAllValuesToDefault() {
        val playerNames = listOf("Player 1", "Player 2")
        viewModel.setPlayerNames(playerNames)

        val numberOfRounds = 6
        viewModel.setNumberOfRounds(numberOfRounds)

        val round1Scores = mapOf(1 to 10, 2 to 20)
        viewModel.setPlayerScores(round1Scores)

        val round2Scores = mapOf(1 to 5, 2 to 15)
        viewModel.setPlayerScores(round2Scores)

        viewModel.reset()

        val uiState = viewModel.uiState.value
        assertEquals(mapOf<Int, String>(), uiState.playerNames)
        assertEquals(1, uiState.currentRound)
        assertEquals(mapOf<Int, Map<Int, Int>>(), uiState.scoreSheet)
        assertEquals(mapOf<Int, Int>(), uiState.totalScores)
        assertEquals(null, uiState.numberOfRounds)
    }
}