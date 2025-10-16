package com.example.playlistmaker.domain.library.impl

import com.example.playlistmaker.domain.library.SelectedTracksInteractor
import com.example.playlistmaker.domain.library.SelectedTracksRepository
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class SelectedTracksInteractorImpl(
    private val selectedTracksRepository: SelectedTracksRepository
) : SelectedTracksInteractor {

    override suspend fun insertTrack(track: Track) {
        selectedTracksRepository.insertTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        selectedTracksRepository.deleteTrack(track)
    }

    override fun getTracks(): Flow<List<Track>> {
        return selectedTracksRepository.getTracks()
    }

}