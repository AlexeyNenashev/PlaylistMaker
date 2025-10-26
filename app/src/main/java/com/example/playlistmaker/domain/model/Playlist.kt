package com.example.playlistmaker.domain.model

data class Playlist(
    val id: Int,
    val name: String,
    val description: String,
    val imageUri: String,
    val trackIds: List<Int>,
)
