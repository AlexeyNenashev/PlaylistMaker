package com.example.playlistmaker.ui.library

import com.example.playlistmaker.domain.model.Playlist

sealed interface PlaylistsState {

    object NoPlaylists : PlaylistsState

    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsState

}