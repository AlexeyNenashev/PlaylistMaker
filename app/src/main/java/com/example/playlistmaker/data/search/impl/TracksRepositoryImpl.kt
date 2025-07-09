package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.network.TracksSearchRequest
import com.example.playlistmaker.data.search.network.TracksSearchResponse
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.util.Resource

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        try {
            val response = networkClient.doRequest(TracksSearchRequest(expression))
            if (response.resultCode != 200) {
                return Resource.Error("Server error")
            }
            val tracks = (response as TracksSearchResponse).results.map {
                Track(
                    it.trackId ?: 0,
                    it.trackName ?: "",
                    it.artistName ?: "",
                    it.trackTime(),
                    it.artworkUrl100 ?: "",
                    it.getCoverArtwork(),
                    it.collectionName ?: "",
                    it.getYear(),
                    it.primaryGenreName ?: "",
                    it.country ?: "",
                    it.previewUrl ?: ""
                )
            }
            return Resource.Success(tracks)
        }
        catch (e: Exception) {
            return Resource.Error("No internet connection")
        }
    }
}