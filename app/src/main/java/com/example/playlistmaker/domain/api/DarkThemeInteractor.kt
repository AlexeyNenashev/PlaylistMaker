package com.example.playlistmaker.domain.api

interface DarkThemeInteractor {
    fun isDarkTheme(darkThemeDefaultValue: Boolean): Boolean
    fun saveDarkTheme(darkTheme: Boolean)
}