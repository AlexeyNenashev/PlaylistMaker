package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SearchHistoryInteractorImpl(
    private val repository: SearchHistoryRepository
) : SearchHistoryInteractor {

    override fun getHistory(): Flow<List<Track>?> {
        return repository.getHistory().map {result -> result.data }
    }

    override suspend fun saveToHistory(t: Track) {
        repository.saveToHistory(t)
    }

    override suspend fun clearHistory() {
        repository.clearHistory()
    }
}