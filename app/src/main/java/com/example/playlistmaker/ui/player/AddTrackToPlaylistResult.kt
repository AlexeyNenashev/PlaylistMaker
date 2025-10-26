package com.example.playlistmaker.ui.player

import com.example.playlistmaker.domain.model.Playlist

sealed interface AddTrackToPlaylistResult {

    data class Success(
        val playlist: Playlist,
        val position: Int
    ) : AddTrackToPlaylistResult

    data class TrackWasAlreadyAdded(
        val playlistName: String
    ) : AddTrackToPlaylistResult

}