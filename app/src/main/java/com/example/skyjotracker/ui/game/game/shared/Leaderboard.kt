package com.example.skyjotracker.ui.game.game.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.skyjotracker.ui.game.theme.SkyjoTrackerTheme

@Composable
fun Leaderboard(
    playerNames: Map<Int, String>,
    totalScores: Map<Int, Int>,
    modifier: Modifier = Modifier
) {
    val sortedPlayerNames = sortPlayersByScore(playerNames, totalScores).toList()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Player",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Score",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        sortedPlayerNames.forEachIndexed { index, (id, name) ->
            val rank = index + 1
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "$rank.",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(40.dp)
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${totalScores[id] ?: 0}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

fun sortPlayersByScore(
    playerNames: Map<Int, String>,
    totalScores: Map<Int, Int>
): Map<Int, String> {
    val sortedScores = totalScores.entries.sortedBy { it.value }
    val sortedPlayerNames = mutableMapOf<Int, String>()
    for (entry in sortedScores) {
        sortedPlayerNames[entry.key] = playerNames[entry.key] ?: ""
    }
    return sortedPlayerNames
}

@Preview
@Composable
fun LeaderboardPreview() {
    SkyjoTrackerTheme(
        darkTheme = true
    ) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            val playerNames = mapOf(
                1 to "Player 1",
                2 to "Player 2",
                3 to "Player 3",
            )
            val totalScores = mapOf(
                1 to 100,
                2 to 150,
                3 to 50,
            )
            Leaderboard(
                playerNames,
                totalScores
            )
        }
    }
}
