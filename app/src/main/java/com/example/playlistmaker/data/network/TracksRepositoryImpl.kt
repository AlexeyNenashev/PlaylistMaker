package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.SearchResult
import com.example.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(expression: String): SearchResult {
        try {
            val response = networkClient.doRequest(TracksSearchRequest(expression))
            val success = (response.resultCode == 200)
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
            return SearchResult(tracks, success)
        }
        catch (e: Exception) {
            return SearchResult(ArrayList(), false)
        }
    }
}