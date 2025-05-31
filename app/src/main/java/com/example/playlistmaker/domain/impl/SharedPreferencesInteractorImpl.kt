package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SharedPreferencesInteractor
import com.example.playlistmaker.domain.models.Track

class SharedPreferencesInteractorImpl : SharedPreferencesInteractor {

    override fun isDarkTheme(darkThemeDefaultValue: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun saveDarkTheme(darkTheme: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getSearchHistory(): Array<Track> {
        TODO("Not yet implemented")
    }

    override fun saveSearchHistory() {
        TODO("Not yet implemented")
    }

}