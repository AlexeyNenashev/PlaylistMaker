package com.example.playlistmaker.data.search.impl

import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.search.StorageClient
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.util.Resource
import kotlinx.coroutines.launch

class SearchHistoryRepositoryImpl(
    private val storage: StorageClient<ArrayList<Track>>,
    private val appDatabase: AppDatabase
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
        //val selectedTrackIDs = appDatabase.trackDao().getTrackIDs()
        //tracks.forEach {
        //    it.isFavorite = selectedTrackIDs.contains(it.trackId)
        //}
        return Resource.Success(tracks)
    }

    override fun clearHistory() {
        storage.storeData(arrayListOf())
    }
}