package com.example.playlistmaker.di

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.library.view_model.PlaylistsViewModel
import com.example.playlistmaker.ui.library.view_model.SelectedTracksViewModel
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

val viewModelModule = module {

    viewModel { (track: Track) ->
        PlayerViewModel(track, get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        SelectedTracksViewModel()
    }

    viewModel {
        PlaylistsViewModel()
    }

}