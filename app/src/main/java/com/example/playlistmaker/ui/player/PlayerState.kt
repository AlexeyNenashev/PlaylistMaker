package com.example.playlistmaker.ui.player

data class PlayerState(
    val isPlaying: Boolean,
    val progressTime: String,
    val artworkUrlCover: String,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val collectionName: String,
    val year: String,
    val primaryGenreName: String,
    val country: String,
    val isFavorite: Boolean
    )
