package com.example.playlistmaker.di

import com.example.playlistmaker.data.sharing.impl.SharingInteractorImpl
import com.example.playlistmaker.domain.library.CreatePlaylistInteractor
import com.example.playlistmaker.domain.library.SelectedTracksInteractor
import com.example.playlistmaker.domain.library.impl.CreatePlaylistInteractorImpl
import com.example.playlistmaker.domain.library.impl.SelectedTracksInteractorImpl
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val interactorModule = module {

    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get(), androidContext())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<SelectedTracksInteractor> {
        SelectedTracksInteractorImpl(get())
    }

    factory<CreatePlaylistInteractor> {
        CreatePlaylistInteractorImpl(get())
    }

}