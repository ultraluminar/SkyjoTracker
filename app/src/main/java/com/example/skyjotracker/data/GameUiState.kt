package com.example.skyjotracker.data


data class GameUiState(
    val playerNames: Map<Int, String> = mapOf(),
    val currentRound: Int = 1,
    val scoreSheet: Map<Int, Map<Int, Int>> = mapOf(),
    val totalScores: Map<Int, Int> = mapOf(),
    val numberOfRounds: Int? = null
)
