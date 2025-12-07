package com.example.skyjotracker.ui.game.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.skyjotracker.R
import com.example.skyjotracker.ui.game.game.shared.Heading
import com.example.skyjotracker.ui.game.game.shared.RoundsProgressIndicator
import com.example.skyjotracker.ui.game.theme.SkyjoTrackerTheme

@Composable
fun ScoringScreen(
    onScoringComplete: (Map<Int, Int>) -> Unit,
    playerNames: Map<Int, String>,
    currentRound: Int,
    numberOfRounds: Int?,
    modifier: Modifier = Modifier
) {
    var playerScoringInput by remember { mutableStateOf(mapOf<Int, String>()) }

    val playerScoring = playerScoringInput.mapValues { it.value.toIntOrNull() ?: 0 }

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
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Heading(
                    mainHeading = stringResource(R.string.game_scoring_heading, currentRound),
                    subHeading = stringResource(R.string.game_scoring_description),
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp)
                )
                RoundsProgressIndicator(
                    currentRound = currentRound,
                    numberOfRounds = numberOfRounds,
                )
            }
            PlayerScoreInputSection(
                playerNames = playerNames,
                playerScoringInput = playerScoringInput,
                onValueChange = { id, newValue ->
                    val updatedMap = playerScoringInput.toMutableMap()
                    updatedMap[id] = newValue
                    playerScoringInput = updatedMap
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Button(
            onClick = { onScoringComplete(playerScoring) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            if (numberOfRounds != null && currentRound == numberOfRounds) {
                Text(stringResource(R.string.game_finish_game))
            } else {
                Text(stringResource(R.string.game_scoring_next_round))
            }
        }
    }
}

@Composable
fun PlayerScoreInputSection(
    playerNames: Map<Int, String>,
    playerScoringInput: Map<Int, String>,
    onValueChange: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        playerNames.forEach { (id, name) ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(8.dp))
                PlayerScoreInputField(
                    value = playerScoringInput[id] ?: "",
                    onValueChange = { newValue ->
                        onValueChange(id, newValue)
                    },
                    modifier = Modifier.widthIn(max = 186.dp)
                )
            }
        }
    }
}

@Composable
fun PlayerScoreInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        label = {
            Text(stringResource(R.string.game_scoring_input_label))
        },
        // leadingIcon = { Icon(painter = painterResource(R.drawable.person_24dp), contentDescription = null) },
        singleLine = true,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun ScoringScreenPreview() {
    SkyjoTrackerTheme {
        ScoringScreen(
            onScoringComplete = {},
            playerNames = mapOf(1 to "Adrian", 2 to "Hinrich", 3 to "Malik"),
            currentRound = 1,
            numberOfRounds = 6,
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}
