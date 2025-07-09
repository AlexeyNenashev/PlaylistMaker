package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TracksRepositoryImpl
import com.example.playlistmaker.data.shared_preferences.DarkThemeRepositoryImpl
import com.example.playlistmaker.data.shared_preferences.SharedPreferencesImpl
import com.example.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.data.sharing.SharingInteractorImpl
import com.example.playlistmaker.data.storage.PrefsStorageClient
import com.example.playlistmaker.data.storage.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.storage.SettingsRepositoryImpl
import com.example.playlistmaker.domain.api.DarkThemeInteractor
import com.example.playlistmaker.domain.api.DarkThemeRepository
import com.example.playlistmaker.domain.api.ExternalNavigator
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.api.SettingsInteractor
import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.api.SharingInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.DarkThemeInteractorImpl
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.models.ThemeSettings
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.App
import com.google.gson.reflect.TypeToken

object Creator {

    private const val DARK_THEME_KEY = "dark_theme"
    private const val HISTORY_KEY = "history"


    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(
            PrefsStorageClient<ArrayList<Track>>(
                context,
                HISTORY_KEY,
                object : TypeToken<ArrayList<Track>>() {}.type
            )
        )
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(ExternalNavigatorImpl(context), context)
    }


    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(
            PrefsStorageClient<ThemeSettings>(
                context,
                DARK_THEME_KEY,
                object : TypeToken<ThemeSettings>() {}.type
            )
        )
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }









    //private val sharingInteractor: SharingInteractor = Creator.ProvideSharingInteractor()
    //private val settingsInteractor: SettingsInteractor = Creator.ProvideSettingsInteractor()






    //private fun getMySharedPreferences(): SharedPreferences {
    //    return App.sharedPrefs
    //}

    //private fun getHistoryRepository(): HistoryRepository {
    //    return HistoryRepositoryImpl(SharedPreferencesImpl(getMySharedPreferences()))
    //}

    //fun provideHistoryInteractor(): HistoryInteractor {
    //    return HistoryInteractorImpl(getHistoryRepository())
    //}

    //private fun getDarkThemeRepository(): DarkThemeRepository {
    //    return DarkThemeRepositoryImpl(SharedPreferencesImpl(getMySharedPreferences()))
    //}

    //fun provideDarkThemeInteractor(): DarkThemeInteractor {
    //    return DarkThemeInteractorImpl(getDarkThemeRepository())
    //}

    //fun providePlayerInteractor(): PlayerInteractor {
    //    return PlayerInteractorImpl(PlayerRepositoryImpl())
    //}

}