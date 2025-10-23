package com.example.playlistmaker.data.db

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConverter(private val gson: Gson) {

    val type = object : TypeToken<ArrayList<Track>>() {}.type

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = 0,
            name = playlist.name,
            description = playlist.description,
            imageUri = playlist.imageUri,
            tracks = gson.toJson(playlist.tracks, type),
            numberOfTracks = playlist.tracks.size
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            name = playlist.name,
            description = playlist.description,
            imageUri = playlist.imageUri,
            tracks = gson.fromJson(playlist.tracks, type)
        )
    }

}