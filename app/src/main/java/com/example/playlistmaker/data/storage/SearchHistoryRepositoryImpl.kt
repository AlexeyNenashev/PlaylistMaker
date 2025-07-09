package com.example.playlistmaker.data.storage

import com.example.playlistmaker.data.StorageClient
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.util.Resource

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
