package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.StorageClient
import com.example.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.storage.PrefsStorageClient
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.data.sharing.impl.SharingInteractorImpl
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<StorageClient<ArrayList<Track>>> {
        PrefsStorageClient<ArrayList<Track>>(get(), get(), "history",
            object : TypeToken<ArrayList<Track>>() {}.type
        )
    }

    single {
        androidContext().getSharedPreferences("preferences", Context.MODE_PRIVATE)
    }

    factory {
        Gson()
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<StorageClient<ThemeSettings>> {
        PrefsStorageClient<ThemeSettings>(get(), get(),"dark_theme",
            object : TypeToken<ThemeSettings>() {}.type
        )
    }

}