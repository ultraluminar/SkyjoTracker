package com.example.skyjotracker.ui.game.screens.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RoundsProgressIndicator(
    numberOfRounds: Int?,
    currentRound: Int,
    modifier: Modifier = Modifier
) {
    if (numberOfRounds != null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
        ) {
            CircularProgressIndicator(
                progress = { currentRound.toFloat() / numberOfRounds.toFloat() },
                strokeWidth = 6.dp,
                modifier = Modifier.size(64.dp)
            )
            Text("$currentRound/$numberOfRounds")
        }
    }
}