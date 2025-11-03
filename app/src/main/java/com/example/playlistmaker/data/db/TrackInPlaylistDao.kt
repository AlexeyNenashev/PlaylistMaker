package com.example.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TrackInPlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackInPlaylistEntity)

    @Update
    suspend fun updateTrack(track: TrackInPlaylistEntity)

    @Query("SELECT * FROM track_in_playlist_table")
    suspend fun getAllTracksInPlaylists(): List<TrackInPlaylistEntity>

    @Query("SELECT * FROM track_in_playlist_table WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: Int): List<TrackInPlaylistEntity>

    @Query("DELETE FROM track_in_playlist_table WHERE trackId = :trackId")
    suspend fun deleteTrackById(trackId: Int)

}