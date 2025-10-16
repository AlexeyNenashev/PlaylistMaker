package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.db.TrackDao
import com.example.playlistmaker.data.search.StorageClient
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchHistoryRepositoryImpl(
    private val storage: StorageClient<ArrayList<Track>>,
    private val trackDao: TrackDao
): SearchHistoryRepository {

    override suspend fun saveToHistory(t: Track) {
        val tracks = storage.getData() ?: arrayListOf()
        tracks.removeAll { it.trackId == t.trackId }
        tracks.add(0, t)
        while (tracks.size > 10) {
            tracks.removeAt(tracks.lastIndex)
        }
        storage.storeData(tracks)
    }

    override fun getHistory(): Flow<Resource<List<Track>>> = flow {
        val tracks = storage.getData() ?: listOf()
        val selectedTrackIDs = trackDao.getTrackIDs()
        tracks.forEach {
            it.isFavorite = selectedTrackIDs.contains(it.trackId)
        }
        emit( Resource.Success(tracks))
    }

    override suspend fun clearHistory() {
        storage.storeData(arrayListOf())
    }
}