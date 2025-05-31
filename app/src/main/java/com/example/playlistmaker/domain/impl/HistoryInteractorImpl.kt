package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.HistoryRepository
import com.example.playlistmaker.domain.models.Track

class HistoryInteractorImpl(private val repository: HistoryRepository) : HistoryInteractor {

    override fun read(items: ArrayList<Track>) {
        repository.read(items)
    }

    override fun save(items: ArrayList<Track>) {
        repository.save(items)
    }

    override fun update(items: ArrayList<Track>, newTrack: Track) {
        repository.update(items, newTrack)
    }

}