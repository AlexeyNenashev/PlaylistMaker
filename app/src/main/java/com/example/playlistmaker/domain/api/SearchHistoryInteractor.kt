package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {

    fun getHistory(consumer: HistoryConsumer)
    fun saveToHistory(t: Track)
    fun clearHistory()

    interface HistoryConsumer {
        fun consume(searchHistory: List<Track>?)
    }
}