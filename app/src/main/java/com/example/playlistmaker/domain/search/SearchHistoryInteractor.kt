package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryInteractor {

    fun getHistory(): Flow<List<Track>?>
    suspend fun saveToHistory(t: Track)
    suspend fun clearHistory()
}