package com.example.playlistmaker.data.library.impl

import com.example.playlistmaker.data.db.PlaylistDao
import com.example.playlistmaker.data.db.PlaylistDbConverter
import com.example.playlistmaker.data.db.PlaylistEntity
import com.example.playlistmaker.data.db.TrackDbConverter
import com.example.playlistmaker.data.db.TrackInPlaylistDao
import com.example.playlistmaker.domain.library.PlaylistRepository
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistDbConverter: PlaylistDbConverter,
    private val trackInPlaylistDao: TrackInPlaylistDao,
    private val trackDbConverter: TrackDbConverter
) : PlaylistRepository {

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistDao.insertPlaylist(playlistDbConverter.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = playlistDao.getPlaylists().sortedByDescending { it.id }
        emit(convertFromPlaylistEntity(playlists))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConverter.map(playlist) }
    }

    override fun addTrackToPlaylist(track: Track, playlist: Playlist): Flow<Playlist> = flow {
        trackInPlaylistDao.insertTrack(trackDbConverter.mapInPlaylist(track))
        val updatedTrackIds = ArrayList<Int>(playlist.trackIds)
        updatedTrackIds.add(track.trackId)
        val updatedPlaylist = Playlist(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.imageUri,
            trackIds = updatedTrackIds)
        playlistDao.updatePlaylist(playlistDbConverter.map(updatedPlaylist))
        emit(updatedPlaylist)
    }

}