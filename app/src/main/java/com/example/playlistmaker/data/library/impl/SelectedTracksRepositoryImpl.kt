package com.example.playlistmaker.data.library.impl

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.TrackDbConverter
import com.example.playlistmaker.data.db.TrackEntity
import com.example.playlistmaker.domain.library.SelectedTracksRepository
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SelectedTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter,
) : SelectedTracksRepository {

    override suspend fun insertTrack(track: Track) {
        appDatabase.trackDao().insertTrack(trackDbConverter.map(track))
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase.trackDao().deleteTrack(trackDbConverter.map(track))
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }

}