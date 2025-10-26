package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [TrackEntity::class, TrackInPlaylistEntity::class, PlaylistEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun trackDao(): TrackDao
    abstract fun trackInPlaylistDao(): TrackInPlaylistDao
    abstract fun playlistDao(): PlaylistDao

}
