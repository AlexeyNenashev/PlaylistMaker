package com.example.playlistmaker.data.db

import com.example.playlistmaker.domain.model.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConverter(private val gson: Gson) {

    val type = object : TypeToken<ArrayList<Int>>() {}.type

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            imageUri = playlist.imageUri,
            trackIds = gson.toJson(playlist.trackIds, type),
            numberOfTracks = playlist.trackIds.size
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            imageUri = playlist.imageUri,
            trackIds = gson.fromJson(playlist.trackIds, type)
        )
    }

}