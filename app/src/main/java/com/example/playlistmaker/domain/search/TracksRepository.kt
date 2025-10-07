package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.util.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}