package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.util.Resource

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}