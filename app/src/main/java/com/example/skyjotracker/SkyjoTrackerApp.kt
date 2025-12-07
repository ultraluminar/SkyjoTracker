package com.example.skyjotracker

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.skyjotracker.ui.game.GameDestination

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
    var currentDestination by rememberSaveable { mutableStateOf(AppDestination.GAME) }

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
                    selected = destination == currentDestination,
                    onClick = { currentDestination = destination }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when(currentDestination) {
                    AppDestination.GAME -> GameDestination()
                    AppDestination.HISTORY -> FavoritesScreen()
                    AppDestination.STATISTICS -> ProfileScreen()
                }
            }
        }
    }
}

@Composable
fun FavoritesScreen() {
    Text("Hier sind deine Favoriten.")
}

@Composable
fun ProfileScreen() {
    Text("Das ist dein Profil.")
}
