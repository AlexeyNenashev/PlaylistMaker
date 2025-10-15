package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.TrackEntity
import com.example.playlistmaker.domain.search.model.Track

class TrackDbConverter {

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.artworkUrlCover,
            track.collectionName,
            track.year,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.artworkUrlCover,
            track.collectionName,
            track.year,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

}