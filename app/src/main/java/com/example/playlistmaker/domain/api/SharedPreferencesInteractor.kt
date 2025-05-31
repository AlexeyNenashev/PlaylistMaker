package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SharedPreferencesInteractor {
    fun isDarkTheme(darkThemeDefaultValue: Boolean): Boolean
    fun saveDarkTheme(darkTheme: Boolean)
    fun getSearchHistory(): Array<Track>
    fun saveSearchHistory()
}