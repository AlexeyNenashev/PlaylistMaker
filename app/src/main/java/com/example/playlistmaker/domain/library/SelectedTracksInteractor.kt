package com.example.playlistmaker.domain.library

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SelectedTracksInteractor {

    suspend fun insertTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    fun getTracks(): Flow<List<Track>>

}