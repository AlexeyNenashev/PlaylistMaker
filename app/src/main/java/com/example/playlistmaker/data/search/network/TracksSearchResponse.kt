package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.dto.TrackDto

class TracksSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()
