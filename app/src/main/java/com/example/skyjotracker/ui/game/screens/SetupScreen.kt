/**
 * Screen von dem aus der Nutzer die Namen von 2 bis 8 Spielern eingeben kann.
 * Auf dem Screen ist oben eine Liste mit Eingabefeldern (zu Beginn sind es 2).
 * Diese Liste lässt sich mit einem Button, der weitere Spieler hinzufügen kann und direkt
 * darunter liegt, erweitern.
 * Am unteren Ende des Screens ist dann ein weiterer Button um zum nächsten Screen zu gelangen.
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.skyjotracker.R
import com.example.skyjotracker.ui.game.screens.shared.Heading
import com.example.skyjotracker.ui.theme.SkyjoTrackerTheme
import kotlin.math.roundToInt

@Composable
fun SetupScreen(
    onStartButtonClick: (List<String>, Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    var playerNameInput by remember { mutableStateOf(listOf("", "")) }
    var customizeNumberOfRounds by remember { mutableStateOf(false) }
    var numberOfRoundsInput by remember { mutableFloatStateOf(6f) }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(playerNameInput.size) {
        if (playerNameInput.size > 2) {
            focusRequester.requestFocus()
        }
    }

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
                mainHeading = stringResource(R.string.game_setup_heading),
                subHeading = stringResource(R.string.game_setup_players_description),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            playerNameInput.forEachIndexed { index, playerName ->
                PlayerNameInputField(
                    value = playerName,
                    playerNumber = index + 1,
                    onValueChange = { newPlayerName ->
                        val updatedList = playerNameInput.toMutableList()
                        updatedList[index] = newPlayerName
                        playerNameInput = updatedList
                    },
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .then(
                            if (index == playerNameInput.lastIndex) {
                                Modifier.focusRequester(focusRequester)
                            } else {
                                Modifier
                            }
                        )
                )
            }
            FilledTonalButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                onClick = {
                    val updatedList = playerNameInput.toMutableList()
                    updatedList.add("")
                    playerNameInput = updatedList
                },
                enabled = playerNameInput.size < 8
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add_24dp),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.game_setup_add_player))
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Customize Game",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondaryFixedDim,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Text(
                text = stringResource(R.string.game_setup_rounds_description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            SwitchRow(
                text = "Set Number of Rounds",
                checked = customizeNumberOfRounds,
                onCheckedChange = {
                    customizeNumberOfRounds = it
                }
            )
            if (customizeNumberOfRounds) {
                val lowerBound = 2
                val upperBound = 16

                SliderRow(
                    value = numberOfRoundsInput,
                    onValueChange = {
                        numberOfRoundsInput = it
                    },
                    lowerBound = lowerBound,
                    upperBound = upperBound
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                val numberOfRounds: Int? = if (customizeNumberOfRounds) {
                    numberOfRoundsInput.roundToInt()
                } else {
                    null
                }
                onStartButtonClick(playerNameInput, numberOfRounds)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text(stringResource(R.string.game_setup_start_game))
        }
    }
}

@Composable
fun PlayerNameInputField(
    value: String,
    playerNumber: Int,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {
    OutlinedTextField(
        value = value,
        label = {
            Text(
                text = stringResource(
                    R.string.game_setup_player_input_label,
                    playerNumber.toString()
                )
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.person_24dp),
                contentDescription = null
            )
        },
        singleLine = true,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun SwitchRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderRow(
    value: Float,
    onValueChange: (Float) -> Unit,
    lowerBound: Int,
    upperBound: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.game_setup_number_of_rounds, value.roundToInt().toString()))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(lowerBound.toString())
            Spacer(modifier = Modifier.width(8.dp))
            Slider(
                value = value,
                onValueChange = onValueChange,
                steps = upperBound - lowerBound - 1,
                valueRange = lowerBound.toFloat()..upperBound.toFloat(),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(upperBound.toString())
        }
    }
}

@Preview
@Composable
fun SetupScreenPreview() {
    SkyjoTrackerTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            SetupScreen(
                onStartButtonClick = { _, _ -> },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}
