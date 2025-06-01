package com.example.playlistmaker.domain.api

interface DarkThemeRepository {
    fun isDarkTheme(darkThemeDefaultValue: Boolean): Boolean
    fun saveDarkTheme(darkTheme: Boolean)
}