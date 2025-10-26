package com.example.playlistmaker.domain.library

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun createPlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    fun addTrackToPlaylist(track: Track, playlist: Playlist): Flow<Playlist>
}