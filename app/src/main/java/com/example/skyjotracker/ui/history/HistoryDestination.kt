package com.example.skyjotracker.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skyjotracker.R
import com.example.skyjotracker.data.database.GameEntity
import com.example.skyjotracker.ui.theme.SkyjoTrackerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryDestination(
    viewModel: HistoryViewModel = viewModel(),
    onResumeClick: (Long) -> Unit,
    onViewClick: (Long) -> Unit
) {
    val games by viewModel.games.collectAsState()
    val filterType by viewModel.filterType.collectAsState()
    val sortType by viewModel.sortType.collectAsState()

    Scaffold(
        topBar = {
            HistoryScreenAppBar(
                filterType = filterType,
                onFilterSelected = viewModel::setFilterType,
                sortType = sortType,
                onSortSelected = viewModel::setSortType
            )
        }
    ) { innerPadding ->
        if (games.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { Text("No games found.") }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(games, key = { it.gameId }) { game ->
                    GameHistoryItem(game, onResumeClick, onViewClick)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreenAppBar(
    filterType: HistoryFilterType,
    onFilterSelected: (HistoryFilterType) -> Unit,
    sortType: HistorySortType,
    onSortSelected: (HistorySortType) -> Unit
) {
    var showFilterMenu by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_destination_history),
                fontWeight = FontWeight.SemiBold
            )
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
        actions = {
            // Filter Action
            Box {
                IconButton(onClick = { showFilterMenu = true }) {
                    Icon(
                        painter = painterResource(R.drawable.filter_list_24dp),
                        contentDescription = "Filter",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                DropdownMenu(
                    expanded = showFilterMenu,
                    onDismissRequest = { showFilterMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("All") },
                        onClick = {
                            onFilterSelected(HistoryFilterType.ALL)
                            showFilterMenu = false
                        },
                        trailingIcon =
                            if (filterType == HistoryFilterType.ALL) {
                                {
                                    Icon(
                                        painterResource(R.drawable.check_24dp),
                                        contentDescription = null
                                    )
                                }
                            } else null
                    )
                    DropdownMenuItem(
                        text = { Text("Finished") },
                        onClick = {
                            onFilterSelected(HistoryFilterType.FINISHED)
                            showFilterMenu = false
                        },
                        trailingIcon =
                            if (filterType == HistoryFilterType.FINISHED) {
                                {
                                    Icon(
                                        painterResource(R.drawable.check_24dp),
                                        contentDescription = null
                                    )
                                }
                            } else null
                    )
                    DropdownMenuItem(
                        text = { Text("In Progress") },
                        onClick = {
                            onFilterSelected(HistoryFilterType.IN_PROGRESS)
                            showFilterMenu = false
                        },
                        trailingIcon =
                            if (filterType == HistoryFilterType.IN_PROGRESS) {
                                {
                                    Icon(
                                        painterResource(R.drawable.check_24dp),
                                        contentDescription = null
                                    )
                                }
                            } else null
                    )
                }
            }

            // Sort Action
            Box {
                IconButton(onClick = { showSortMenu = true }) {
                    Icon(
                        painter = painterResource(R.drawable.sort_24dp),
                        contentDescription = "Sort",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Newest First") },
                        onClick = {
                            onSortSelected(HistorySortType.NEWEST_FIRST)
                            showSortMenu = false
                        },
                        trailingIcon =
                            if (sortType == HistorySortType.NEWEST_FIRST) {
                                {
                                    Icon(
                                        painterResource(R.drawable.check_24dp),
                                        contentDescription = null
                                    )
                                }
                            } else null
                    )
                    DropdownMenuItem(
                        text = { Text("Oldest First") },
                        onClick = {
                            onSortSelected(HistorySortType.OLDEST_FIRST)
                            showSortMenu = false
                        },
                        trailingIcon =
                            if (sortType == HistorySortType.OLDEST_FIRST) {
                                {
                                    Icon(
                                        painterResource(R.drawable.check_24dp),
                                        contentDescription = null
                                    )
                                }
                            } else null
                    )
                }
            }
        }
    )
}

@Composable
fun GameHistoryItem(game: GameEntity, onResumeClick: (Long) -> Unit, onViewClick: (Long) -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Game #${game.gameId}", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = if (game.isFinished) "Finished" else "In Progress",
                    style = MaterialTheme.typography.labelMedium,
                    color =
                        if (game.isFinished) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatDate(game.timestamp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                if (game.isFinished) {
                    androidx.compose.material3.OutlinedButton(
                        onClick = { onViewClick(game.gameId) }
                    ) { Text("View Results") }
                } else {
                    androidx.compose.material3.Button(onClick = { onResumeClick(game.gameId) }) {
                        Text("Resume")
                    }
                }
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return formatter.format(date)
}

@Preview
@Composable
fun GameHistoryItemPreviewFinished() {
    SkyjoTrackerTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            GameHistoryItem(
                game =
                    GameEntity(
                        gameId = 123,
                        timestamp = System.currentTimeMillis(),
                        isFinished = true
                    ),
                onResumeClick = {},
                onViewClick = {}
            )
        }
    }
}

@Preview
@Composable
fun GameHistoryItemPreviewInProgress() {
    SkyjoTrackerTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            GameHistoryItem(
                game =
                    GameEntity(
                        gameId = 456,
                        timestamp = System.currentTimeMillis(),
                        isFinished = false
                    ),
                onResumeClick = {},
                onViewClick = {}
            )
        }
    }
}
