package com.example.skyjotracker.ui.game.screens.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.skyjotracker.ui.theme.SkyjoTrackerTheme

private val cellWidth: Dp = 72.dp
private val cellHeight: Dp = 48.dp


@Composable
fun ScoringTable(
    playerNames: Map<Int, String>,
    scoreSheet: Map<Int, Map<Int, Int>>,
    totalScores: Map<Int, Int>,
    modifier: Modifier = Modifier
) {
    val sortedPlayerIds = playerNames.keys.sorted()
    val rounds = scoreSheet.keys.sorted()

    LazyRow(modifier = modifier) {
        item {
            Box(contentAlignment = Alignment.BottomCenter) {
                Column {
                    HeaderRow(playerNames, sortedPlayerIds)

                    // Vertically scrollable list for the score rows
                    LazyColumn(modifier = Modifier.padding(bottom = cellHeight)) {
                        // Score Rows for each round
                        items(rounds) { round ->
                            ScoreRow(round, scoreSheet, sortedPlayerIds)
                        }
                    }
                }
                FooterRow(totalScores, sortedPlayerIds)
            }
        }
    }
}

@Composable
private fun HeaderRow(playerNames: Map<Int, String>, sortedPlayerIds: List<Int>) {
    Row {
        // Empty cell for the top-left corner
        TableCell(text = "", isHeader = true)
        sortedPlayerIds.forEach { playerId ->
            TableCell(text = playerNames[playerId] ?: "", isHeader = true)
        }
    }
}

@Composable
private fun ScoreRow(round: Int, scoreSheet: Map<Int, Map<Int, Int>>, sortedPlayerIds: List<Int>) {
    Row {
        TableCell(text = round.toString(), isHeader = true)
        sortedPlayerIds.forEach { playerId ->
            val score = scoreSheet[round]?.get(playerId)?.toString() ?: "0"
            TableCell(text = score)
        }
    }
}

@Composable
private fun FooterRow(totalScores: Map<Int, Int>, sortedPlayerIds: List<Int>) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        TableCell(text = "Total", isHeader = true)
        sortedPlayerIds.forEach { playerId ->
            val total = totalScores[playerId]?.toString() ?: "0"
            TableCell(text = total, isTotal = true)
        }
    }
}

@Composable
private fun TableCell(
    text: String,
    isHeader: Boolean = false,
    isTotal: Boolean = false
) {
    Box(
        modifier = Modifier
            .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
            .height(cellHeight)
            .width(cellWidth)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontWeight = if (isHeader || isTotal) FontWeight.Bold else FontWeight.Normal,
            color = if (isTotal) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScoringTablePreview() {
    SkyjoTrackerTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            ScoringTable(
                playerNames = mapOf(1 to "Alice", 2 to "Bob"),
                scoreSheet = mapOf(
                    1 to mapOf(1 to 10, 2 to 5),
                    2 to mapOf(1 to -2, 2 to 15),
                    3 to mapOf(1 to 20, 2 to 20)
                ),
                totalScores = mapOf(1 to 28, 2 to 40)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 300)
@Composable
fun ScoringTableScrollPreview() {
    SkyjoTrackerTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            ScoringTable(
                playerNames = mapOf(1 to "P1", 2 to "P2", 3 to "P3", 4 to "P4", 5 to "P5"),
                scoreSheet = mapOf(
                    1 to mapOf(1 to 10, 2 to 5, 3 to 8, 4 to -1, 5 to 12),
                    2 to mapOf(1 to -2, 2 to 15, 3 to 3, 4 to 22, 5 to 7),
                    3 to mapOf(1 to 20, 2 to 20, 3 to 11, 4 to 21, 5 to 19)
                ),
                totalScores = mapOf(1 to 8, 2 to 20, 3 to 11, 4 to 21, 5 to 19)
            )
        }
    }
}