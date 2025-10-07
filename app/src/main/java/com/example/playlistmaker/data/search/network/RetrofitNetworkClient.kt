package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

class RetrofitNetworkClient(retrofit: Retrofit) : NetworkClient {

    private val iTunesService = retrofit.create(ItunesApiService::class.java)

    override suspend fun doRequest(dto: Any): Response {
        return if (dto is TracksSearchRequest) {
            withContext(Dispatchers.IO) {
                try {
                    val response = iTunesService.search(dto.expression)
                    response.apply { resultCode = 200 }
                } catch (e: Throwable) {
                    Response().apply { resultCode = 500 }
                }
            }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}