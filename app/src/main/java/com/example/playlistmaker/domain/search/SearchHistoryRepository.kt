package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    suspend fun saveToHistory(t: Track)
    fun getHistory(): Flow<Resource<List<Track>>>
    suspend fun clearHistory()
}