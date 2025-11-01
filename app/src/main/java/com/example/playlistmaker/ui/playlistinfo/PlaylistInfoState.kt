package com.example.playlistmaker.ui.playlistinfo

import com.example.playlistmaker.domain.model.Track

data class PlaylistInfoState(
    val name: String,
    val description: String,
    val imageFileName: String,
    val howManyMinutes: Int,
    val howManyTracks: Int,
    val tracks: List<Track>
)
