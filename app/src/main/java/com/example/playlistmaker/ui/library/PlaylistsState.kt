package com.example.playlistmaker.ui.library

sealed interface PlaylistsState {
    object NoPlaylists : PlaylistsState
}