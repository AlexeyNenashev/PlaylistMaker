package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TracksRepositoryImpl
import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.data.shared_preferences.DarkThemeRepositoryImpl
import com.example.playlistmaker.data.shared_preferences.HistoryRepositoryImpl
import com.example.playlistmaker.data.shared_preferences.SharedPreferencesImpl
import com.example.playlistmaker.domain.api.DarkThemeInteractor
import com.example.playlistmaker.domain.api.DarkThemeRepository
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.HistoryRepository
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.DarkThemeInteractorImpl
import com.example.playlistmaker.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.ui.App

object Creator {

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getMySharedPreferences(): SharedPreferences {
        return App.sharedPrefs
    }

    private fun getHistoryRepository(): HistoryRepository {
        return HistoryRepositoryImpl(SharedPreferencesImpl(getMySharedPreferences()))
    }

    fun provideHistoryInteractor(): HistoryInteractor {
        return HistoryInteractorImpl(getHistoryRepository())
    }

    private fun getDarkThemeRepository(): DarkThemeRepository {
        return DarkThemeRepositoryImpl(SharedPreferencesImpl(getMySharedPreferences()))
    }

    fun provideDarkThemeInteractor(): DarkThemeInteractor {
        return DarkThemeInteractorImpl(getDarkThemeRepository())
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(PlayerRepositoryImpl())
    }

}