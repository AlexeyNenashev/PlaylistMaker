package com.example.playlistmaker.domain.model

data class Playlist(
    val name: String,
    val description: String,
    val imageUri: String,
    val tracks: List<Track>,
)
