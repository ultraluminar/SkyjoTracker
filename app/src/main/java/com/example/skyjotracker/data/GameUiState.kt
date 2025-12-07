package com.example.skyjotracker.data

data class GameUiState(
    // Map of player id to player name
    val playerNames: Map<Int, String> = mapOf(),
    val currentRound: Int = 1,
    // Map of round number to map of player id to score
    val scoreSheet: Map<Int, Map<Int, Int>> = mapOf(),
    // Map of player id to total score
    val totalScores: Map<Int, Int> = mapOf(),
    val numberOfRounds: Int? = null
)
