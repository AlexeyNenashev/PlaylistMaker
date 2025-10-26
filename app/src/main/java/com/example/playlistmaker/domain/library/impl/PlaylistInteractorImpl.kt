package com.example.playlistmaker.domain.library.impl

import com.example.playlistmaker.domain.library.PlaylistInteractor
import com.example.playlistmaker.domain.library.PlaylistRepository
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistRepository.createPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override fun addTrackToPlaylist(track: Track, playlist: Playlist): Flow<Playlist> {
        return playlistRepository.addTrackToPlaylist(track, playlist)
    }

}