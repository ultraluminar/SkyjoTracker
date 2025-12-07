package com.example.skyjotracker.ui.game

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.skyjotracker.R
import com.example.skyjotracker.ui.game.screens.FinishScreen
import com.example.skyjotracker.ui.game.screens.RoundScreen
import com.example.skyjotracker.ui.game.screens.ScoringScreen
import com.example.skyjotracker.ui.game.screens.SetupScreen

enum class GameScreen(
    @StringRes val title: Int,
) {
    SETUP(title = R.string.game_setup),
    ROUND(title = R.string.game_round),
    SCORING(title = R.string.game_scoring),
    FINISH(title = R.string.game_finish)
}

@Composable
fun GameDestination(
    viewModel: GameViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = GameScreen.valueOf(
        backStackEntry?.destination?.route ?: GameScreen.SETUP.name
    )
    var showCreditDialog by remember { mutableStateOf(false) }

    if (showCreditDialog) {
        AppCreditsDialog(
            onConfirm = { showCreditDialog = false },
            onDismiss = { showCreditDialog = false }
        )
    }

    Scaffold(
        topBar = {
            GameScreenAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                onCreditsClick = { showCreditDialog = true }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = GameScreen.SETUP.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = GameScreen.SETUP.name) {
                SetupScreen(
                    onStartButtonClick = { playerNames, numberOfRounds ->
                        viewModel.setPlayerNames(playerNames)
                        viewModel.setNumberOfRounds(numberOfRounds)
                        navController.navigate(GameScreen.ROUND.name)
                    }
                )
            }
            composable(route = GameScreen.ROUND.name) {
                RoundScreen(
                    onFinishGame = {
                        navController.navigate(GameScreen.FINISH.name)
                    },
                    onRoundComplete = {
                        navController.navigate(GameScreen.SCORING.name)
                    },
                    playerNames = uiState.playerNames,
                    currentRound = uiState.currentRound,
                    numberOfRounds = uiState.numberOfRounds,
                    scoreSheet = uiState.scoreSheet,
                    totalScores = uiState.totalScores
                )
            }
            composable(route = GameScreen.SCORING.name) {
                val lastRound = uiState.currentRound == uiState.numberOfRounds
                val route = if (lastRound) {
                    GameScreen.FINISH.name
                } else {
                    GameScreen.ROUND.name
                }
                ScoringScreen(
                    onScoringComplete = {
                        viewModel.setPlayerScores(it)
                        navController.navigate(route)
                    },
                    playerNames = uiState.playerNames,
                    currentRound = uiState.currentRound,
                    numberOfRounds = uiState.numberOfRounds
                )
            }
            composable(route = GameScreen.FINISH.name) {
                FinishScreen(
                    onNewGameButtonClick = {
                        viewModel.reset()
                        navController.popBackStack(GameScreen.SETUP.name, inclusive = false)
                    },
                    playerNames = uiState.playerNames,
                    totalScores = uiState.totalScores
                )
            }
        }
    }
}

@Composable
fun AppCreditsDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.app_credits_title))
        },
        text = {
            Text(text = stringResource(R.string.app_credits_text))
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = stringResource(R.string.confirm))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreenAppBar(
    currentScreen: GameScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onCreditsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = stringResource(currentScreen.title),
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back_24dp),
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        actions = {
            IconButton(
                onClick = onCreditsClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.info_24dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentDescription = stringResource(R.string.app_info)
                )
            }
        },
        modifier = modifier
    )
}
