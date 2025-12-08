package com.example.skyjotracker.ui.game.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.SecondaryTabRow
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
import com.example.skyjotracker.ui.game.screens.shared.ScoringTable
import com.example.skyjotracker.ui.theme.SkyjoTrackerTheme

@Composable
fun FinishScreen(
    onNewGameButtonClick: () -> Unit,
    playerNames: Map<Int, String>,
    totalScores: Map<Int, Int>,
    scoreSheet: Map<Int, Map<Int, Int>>, // Added parameter
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
            Heading(
                mainHeading = stringResource(R.string.game_finish_heading),
                subHeading = stringResource(R.string.game_finish_description),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            SecondaryTabRow(selectedTabIndex = selectedVisualizer) {
                ScoreVisualizer.entries.forEachIndexed { index, visualizer ->
                    Tab(
                        selected = selectedVisualizer == index,
                        onClick = { selectedVisualizer = index },
                        text = { Text(stringResource(visualizer.label)) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            when (selectedVisualizer) {
                ScoreVisualizer.LEADERBOARD.ordinal ->
                    Leaderboard(
                        playerNames = playerNames,
                        totalScores = totalScores,
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    )

                ScoreVisualizer.TABLE.ordinal ->
                    ScoringTable(
                        playerNames = playerNames,
                        scoreSheet = scoreSheet, // Pass scoreSheet here
                        totalScores = totalScores
                    )
            }
        }
        Button(
            onClick = { onNewGameButtonClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) { Text(stringResource(R.string.game_finish_new_game)) }
    }
}

@Preview
@Composable
fun FinishScreenPreview() {
    SkyjoTrackerTheme {
        FinishScreen(
            onNewGameButtonClick = {},
            playerNames = mapOf(1 to "Adrian", 2 to "Hinrich", 3 to "Malik"),
            totalScores = mapOf(1 to 10, 2 to 5, 3 to 35),
            scoreSheet =
                mapOf(
                    1 to mapOf(1 to 10, 2 to 5, 3 to 8, 4 to -1, 5 to 12),
                    2 to mapOf(1 to -2, 2 to 15, 3 to 3, 4 to 22, 5 to 7),
                    3 to mapOf(1 to 20, 2 to 20, 3 to 11, 4 to 21, 5 to 19)
                ),
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}
