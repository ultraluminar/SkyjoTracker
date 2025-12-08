package com.example.skyjotracker

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.skyjotracker.ui.game.GameDestination
import com.example.skyjotracker.ui.game.GameViewModel
import com.example.skyjotracker.ui.history.HistoryDestination
import com.example.skyjotracker.ui.history.HistoryViewModel

enum class AppDestination(
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
) {
    GAME(label = R.string.app_destination_game, icon = R.drawable.playing_cards_24dp),
    HISTORY(label = R.string.app_destination_history, icon = R.drawable.history_24dp),
    STATISTICS(label = R.string.app_destination_statistics, icon = R.drawable.analytics_24dp),
}

// @PreviewScreenSizes
@Composable
fun SkyjoTrackerApp() {
    val context = LocalContext.current
    val app = context.applicationContext as SkyjoApplication
    val gameViewModel: GameViewModel = viewModel(factory = GameViewModel.Factory(app.repository))
    val historyViewModel: HistoryViewModel =
        viewModel(factory = HistoryViewModel.Factory(app.repository))

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestination.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            painter = painterResource(destination.icon),
                            contentDescription = stringResource(destination.label)
                        )
                    },
                    label = { Text(stringResource(destination.label)) },
                    selected = currentDestination == destination.name,
                    onClick = {
                        navController.navigate(destination.name) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = AppDestination.GAME.name,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(AppDestination.GAME.name) { GameDestination(viewModel = gameViewModel) }
            composable(AppDestination.HISTORY.name) {
                HistoryDestination(viewModel = historyViewModel)
            }
            composable(AppDestination.STATISTICS.name) { StatisticsDestination() }
        }
    }
}

@Composable
fun StatisticsDestination() {
    Text("Here you can see your statistics.")
}
