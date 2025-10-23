package com.example.playlistmaker.domain.library

import com.example.playlistmaker.domain.model.Playlist

interface PlaylistRepository {
    suspend fun createPlaylist(playlist: Playlist)
}