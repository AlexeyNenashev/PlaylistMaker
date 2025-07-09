package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.util.Resource

interface SearchHistoryRepository {
    fun saveToHistory(t: Track)
    fun getHistory(): Resource<List<Track>>
    fun clearHistory()
}