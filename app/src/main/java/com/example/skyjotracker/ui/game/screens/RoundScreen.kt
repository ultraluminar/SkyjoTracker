/**
 * Screen auf dem den Spielern währende der Runde der aktuelle Spielstand angezeigt wird.
 * Ein Liste mit den Namen der Spieler links und den aktuellen Spielständen daneben.
 */

package com.example.skyjotracker.ui.game.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.skyjotracker.R
import com.example.skyjotracker.ui.game.screens.shared.Heading
import com.example.skyjotracker.ui.game.screens.shared.Leaderboard
import com.example.skyjotracker.ui.game.screens.shared.RoundsProgressIndicator
import com.example.skyjotracker.ui.game.screens.shared.ScoringTable
import com.example.skyjotracker.ui.game.theme.SkyjoTrackerTheme

enum class ScoreVisualizer(
    val label: String
) {
    LEADERBOARD("Leaderboard"),
    TABLE("Table")
}

@Composable
fun RoundScreen(
    onRoundComplete: () -> Unit,
    onFinishGame: () -> Unit,
    playerNames: Map<Int, String>,
    currentRound: Int,
    numberOfRounds: Int?,
    scoreSheet: Map<Int, Map<Int, Int>>,
    totalScores: Map<Int, Int>,
    modifier: Modifier = Modifier
) {
    val defaultVisualizer = ScoreVisualizer.LEADERBOARD
    var selectedVisualizer by rememberSaveable { mutableIntStateOf(defaultVisualizer.ordinal) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(horizontal = 28.dp)
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .weight(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Heading(
                    mainHeading = stringResource(R.string.game_round_heading, currentRound),
                    subHeading = stringResource(R.string.game_round_description),
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp)
                )
                RoundsProgressIndicator(
                    numberOfRounds = numberOfRounds,
                    currentRound = currentRound
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            SecondaryTabRow(selectedTabIndex = selectedVisualizer) {
                ScoreVisualizer.entries.forEachIndexed { index, visualizer ->
                    Tab(
                        selected = selectedVisualizer == index,
                        onClick = { selectedVisualizer = index },
                        text = { Text(visualizer.label) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            when (selectedVisualizer) {
                ScoreVisualizer.LEADERBOARD.ordinal -> Leaderboard(
                    playerNames = playerNames,
                    totalScores = totalScores,
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
                ScoreVisualizer.TABLE.ordinal -> ScoringTable(
                    playerNames = playerNames,
                    scoreSheet = scoreSheet,
                    totalScores = totalScores
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onRoundComplete,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.game_round_enter_scores))
            }
            if (numberOfRounds == null) {
                Spacer(
                    modifier = Modifier.height(6.dp)
                )
                FilledTonalButton(
                    onClick = onFinishGame,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.game_finish_game))
                }
            }
            Spacer(
                modifier = Modifier.height(32.dp)
            )
        }
    }
}

@Preview
@Composable
fun RoundScreenPreviewSetNumberOfRounds() {
    SkyjoTrackerTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            RoundScreen(
                onRoundComplete = {},
                onFinishGame = {},
                playerNames = mapOf(1 to "Adrian", 2 to "Hinrich", 3 to "Malik"),
                currentRound = 1,
                numberOfRounds = 6,
                scoreSheet = mapOf(
                    1 to mapOf(1 to 10, 2 to 5, 3 to 8, 4 to -1, 5 to 12),
                    2 to mapOf(1 to -2, 2 to 15, 3 to 3, 4 to 22, 5 to 7),
                    3 to mapOf(1 to 20, 2 to 20, 3 to 11, 4 to 21, 5 to 19)
                ),
                totalScores = mapOf(1 to 10, 2 to 5, 3 to 35),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}

@Preview
@Composable
fun RoundScreenPreviewWithoutNumberOfRounds() {
    SkyjoTrackerTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            RoundScreen(
                onRoundComplete = {},
                onFinishGame = {},
                playerNames = mapOf(1 to "Adrian", 2 to "Hinrich", 3 to "Malik"),
                currentRound = 1,
                numberOfRounds = null,
                scoreSheet = mapOf(
                    1 to mapOf(1 to 10, 2 to 5, 3 to 8, 4 to -1, 5 to 12),
                    2 to mapOf(1 to -2, 2 to 15, 3 to 3, 4 to 22, 5 to 7),
                    3 to mapOf(1 to 20, 2 to 20, 3 to 11, 4 to 21, 5 to 19),
                    4 to mapOf(1 to 20, 2 to 20, 3 to 11, 4 to 21, 5 to 19),
                    5 to mapOf(1 to 20, 2 to 20, 3 to 11, 4 to 21, 5 to 16),
                    6 to mapOf(1 to 20, 2 to 20, 3 to 11, 4 to 21, 5 to 37),
                    7 to mapOf(1 to 10, 2 to 5, 3 to 8, 4 to -1, 5 to 12),
                    8 to mapOf(1 to -2, 2 to 15, 3 to 3, 4 to 22, 5 to 7),
                    9 to mapOf(1 to 20, 2 to 20, 3 to 11, 4 to 21, 5 to 19),
                ),
                totalScores = mapOf(1 to 10, 2 to 5, 3 to 35),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}
