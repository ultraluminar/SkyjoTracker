package com.example.skyjotracker.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skyjotracker.data.GameRepository
import com.example.skyjotracker.data.database.GameEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: GameRepository) : ViewModel() {

    private val _filterType = MutableStateFlow(HistoryFilterType.ALL)
    val filterType: StateFlow<HistoryFilterType> = _filterType

    private val _sortType = MutableStateFlow(HistorySortType.NEWEST_FIRST)
    val sortType: StateFlow<HistorySortType> = _sortType

    val games: StateFlow<List<GameEntity>> =
        combine(repository.getAllGamesStream(), _filterType, _sortType) { games, filter, sort ->
            val filteredGames =
                when (filter) {
                    HistoryFilterType.ALL -> games
                    HistoryFilterType.FINISHED -> games.filter { it.isFinished }
                    HistoryFilterType.IN_PROGRESS -> games.filter { !it.isFinished }
                }

            when (sort) {
                HistorySortType.NEWEST_FIRST ->
                    filteredGames.sortedByDescending { it.timestamp }

                HistorySortType.OLDEST_FIRST -> filteredGames.sortedBy { it.timestamp }
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun setFilterType(type: HistoryFilterType) {
        _filterType.value = type
    }

    fun setSortType(type: HistorySortType) {
        _sortType.value = type
    }

    fun updateGameImage(gameId: Long, imageUri: String?) {
        viewModelScope.launch { repository.updateGameImage(gameId, imageUri) }
    }

    class Factory(private val repository: GameRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
                return HistoryViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

enum class HistoryFilterType {
    ALL,
    FINISHED,
    IN_PROGRESS
}

enum class HistorySortType {
    NEWEST_FIRST,
    OLDEST_FIRST
}
