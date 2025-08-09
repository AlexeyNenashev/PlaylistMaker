package com.example.playlistmaker.di

import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

val viewModelModule = module {

    viewModel { (url: String) ->
        PlayerViewModel(url, get())
    }

    viewModel {
        SearchViewModel(get(), get(), androidContext())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

}