package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.SearchResult

interface TracksRepository {
    fun searchTracks(expression: String): SearchResult
}