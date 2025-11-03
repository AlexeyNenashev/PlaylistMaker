package com.example.playlistmaker.data.library.impl

import android.util.Log
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

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.updatePlaylist(playlistDbConverter.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = playlistDao.getPlaylists().sortedByDescending { it.id }
        emit(convertFromPlaylistEntity(playlists))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConverter.map(playlist) }
    }

    override fun addTrackToPlaylist(track: Track, playlist: Playlist): Flow<Playlist> = flow {
        addTrackToTable(track)
        val updatedTrackIds = ArrayList<Int>(playlist.trackIds)
        updatedTrackIds.add(track.trackId)
        val updatedPlaylist = playlist.copy(trackIds = updatedTrackIds)
        playlistDao.updatePlaylist(playlistDbConverter.map(updatedPlaylist))
        emit(updatedPlaylist)
    }

    override fun getPlaylistAndItsTracksById(playlistId: Int, doTracksRequest: Boolean): Flow<Pair<Playlist, List<Track>>> = flow {
        val playlist: Playlist = playlistDbConverter.map(
            playlistDao.getPlaylistById(playlistId)
        )
        if (doTracksRequest) {
            val tracksInPlaylists = trackInPlaylistDao.getAllTracksInPlaylists()
                .filter { it.trackId in playlist.trackIds }
                .map { track -> trackDbConverter.mapInPlaylist(track) }
            emit(Pair(playlist, tracksInPlaylists))
        }
        else {
            emit(Pair(playlist, emptyList()))
        }
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Int, playlistId: Int) {
        deleteTrackFromTable(trackId)
        val playlist: Playlist = playlistDbConverter.map(
            playlistDao.getPlaylistById(playlistId)
        )
        val updatedTrackIds = ArrayList<Int>(playlist.trackIds)
        updatedTrackIds.remove(trackId)
        val updatedPlaylist = playlist.copy(trackIds = updatedTrackIds)
        playlistDao.updatePlaylist(playlistDbConverter.map(updatedPlaylist))
    }

    override suspend fun deletePlaylistById(playlistId: Int) {
        //Log.d("playlists","deleting playlist $playlistId...")
        val playlist: Playlist = playlistDbConverter.map(
            playlistDao.getPlaylistById(playlistId)
        )
        playlist.trackIds.forEach { trackId ->
            deleteTrackFromTable(trackId)
        }
        playlistDao.deletePlaylistById(playlistId)
        //Log.d("playlists","Playlist $playlistId deleted.")
    }

    private suspend fun addTrackToTable(track: Track) {
        val tracksInTable = trackInPlaylistDao.getTrackById(track.trackId)
        if (tracksInTable.isEmpty()) {
            trackInPlaylistDao.insertTrack(trackDbConverter.mapInPlaylist(track))
            //Log.d("playlists","Track ${track.trackId} added (count = 1)")
        } else {
            val updatedPlaylistsCount = tracksInTable[0].playlistsCount + 1
            trackInPlaylistDao.updateTrack(tracksInTable[0].copy(playlistsCount = updatedPlaylistsCount))
            //Log.d("playlists","Track ${track.trackId} incremented (count = $updatedPlaylistsCount)")
        }
    }

    private suspend fun deleteTrackFromTable(trackId: Int) {
        val tracksInTable = trackInPlaylistDao.getTrackById(trackId)
        if (tracksInTable.isNotEmpty()) {
            val updatedPlaylistsCount = tracksInTable[0].playlistsCount - 1
            if (updatedPlaylistsCount > 0) {
                trackInPlaylistDao.updateTrack(tracksInTable[0].copy(playlistsCount = updatedPlaylistsCount))
                //Log.d("playlists","Track $trackId decremented (count = $updatedPlaylistsCount)")
            } else {
                trackInPlaylistDao.deleteTrackById(trackId)
                //Log.d("playlists","Track $trackId deleted")
            }
        }
    }

}