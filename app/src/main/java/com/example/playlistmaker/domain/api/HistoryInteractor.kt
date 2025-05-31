package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface HistoryInteractor {
    fun read(items: ArrayList<Track>)
    fun save(items: ArrayList<Track>)
    fun update(items: ArrayList<Track>, newTrack: Track)
}