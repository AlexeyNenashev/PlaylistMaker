package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.StorageClient
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.util.Resource

class SearchHistoryRepositoryImpl(
    private val storage: StorageClient<ArrayList<Track>>
): SearchHistoryRepository {

    override fun saveToHistory(t: Track) {
        val tracks = storage.getData() ?: arrayListOf()
        tracks.removeAll { it.trackId == t.trackId }
        tracks.add(0, t)
        while (tracks.size > 10) {
            tracks.removeAt(tracks.lastIndex)
        }
        storage.storeData(tracks)
    }

    override fun getHistory(): Resource<List<Track>> {
        val tracks = storage.getData() ?: listOf()
        return Resource.Success(tracks)
    }

    override fun clearHistory() {
        storage.storeData(arrayListOf())
    }
}