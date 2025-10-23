package com.example.playlistmaker.domain.library.impl

import com.example.playlistmaker.domain.library.CreatePlaylistInteractor
import com.example.playlistmaker.domain.library.PlaylistRepository
import com.example.playlistmaker.domain.model.Playlist

class CreatePlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    CreatePlaylistInteractor {

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistRepository.createPlaylist(playlist)
    }

}