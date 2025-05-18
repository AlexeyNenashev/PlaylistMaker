package com.example.playlistmaker

import com.example.playlistmaker.data.network.ItunesApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient() {
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ItunesApiService::class.java)
    fun getITunesService(): ItunesApiService {
        return iTunesService
    }
}