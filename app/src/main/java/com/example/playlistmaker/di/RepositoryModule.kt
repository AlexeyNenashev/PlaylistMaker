package com.example.playlistmaker.di

import com.example.playlistmaker.data.db.TrackDbConverter
import com.example.playlistmaker.data.library.impl.SelectedTracksRepositoryImpl
import com.example.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.library.SelectedTracksRepository
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(qualifier = named("historyStorage")), get())
    }

    factory<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    factory<SettingsRepository> {
        SettingsRepositoryImpl(get(qualifier = named("themeStorage")))
    }

    factory<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    factory { TrackDbConverter() }

    factory<SelectedTracksRepository> {
        SelectedTracksRepositoryImpl(get(), get())
    }

}