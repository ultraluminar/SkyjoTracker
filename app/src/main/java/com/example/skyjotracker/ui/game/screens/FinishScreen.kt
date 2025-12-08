package com.example.skyjotracker.ui.game.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.skyjotracker.R
import com.example.skyjotracker.ui.game.screens.shared.Heading
import com.example.skyjotracker.ui.game.screens.shared.Leaderboard
import com.example.skyjotracker.ui.theme.SkyjoTrackerTheme

@Composable
fun FinishScreen(
    onNewGameButtonClick: () -> Unit,
    playerNames: Map<Int, String>,
    totalScores: Map<Int, Int>,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(horizontal = 28.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            Heading(
                mainHeading = stringResource(R.string.game_finish_heading),
                subHeading = stringResource(R.string.game_finish_description),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Leaderboard(
                playerNames = playerNames,
                totalScores = totalScores
            )
        }
        Button(
            onClick = { onNewGameButtonClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text(stringResource(R.string.game_finish_new_game))
        }
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
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}