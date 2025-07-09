package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.util.Resource

interface SearchHistoryRepository {
    fun saveToHistory(t: Track)
    fun getHistory(): Resource<List<Track>>
    fun clearHistory()
}