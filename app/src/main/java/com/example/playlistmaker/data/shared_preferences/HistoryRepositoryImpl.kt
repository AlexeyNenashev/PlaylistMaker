package com.example.playlistmaker.data.shared_preferences

import com.example.playlistmaker.domain.api.HistoryRepository
import com.example.playlistmaker.domain.models.Track

class HistoryRepositoryImpl(private val sharedPreferencesImpl: SharedPreferencesImpl) : HistoryRepository {

    override fun read(items: ArrayList<Track>) {
        items.clear()
        items += sharedPreferencesImpl.getSearchHistory()
    }

    override fun save(items: ArrayList<Track>) {
        sharedPreferencesImpl.saveSearchHistory(items)
    }

    override fun update(items: ArrayList<Track>, newTrack: Track) {
        items.removeAll { it.trackId == newTrack.trackId }
        items.add(0, newTrack)
        while (items.size > 10) {
            items.removeLast()
        }
    }

}