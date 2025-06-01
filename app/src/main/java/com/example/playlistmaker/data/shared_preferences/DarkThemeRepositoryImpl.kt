package com.example.playlistmaker.data.shared_preferences

import com.example.playlistmaker.domain.api.DarkThemeRepository

class DarkThemeRepositoryImpl(private val sharedPreferencesImpl: SharedPreferencesImpl) :
    DarkThemeRepository {

    override fun isDarkTheme(darkThemeDefaultValue: Boolean): Boolean {
        return sharedPreferencesImpl.isDarkTheme(darkThemeDefaultValue)
    }

    override fun saveDarkTheme(darkTheme: Boolean) {
        sharedPreferencesImpl.saveDarkTheme(darkTheme)
    }

}