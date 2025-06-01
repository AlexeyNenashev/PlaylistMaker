package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.DarkThemeInteractor
import com.example.playlistmaker.domain.api.DarkThemeRepository

class DarkThemeInteractorImpl(private val repository: DarkThemeRepository) : DarkThemeInteractor {

    override fun isDarkTheme(darkThemeDefaultValue: Boolean): Boolean {
        return repository.isDarkTheme(darkThemeDefaultValue)
    }

    override fun saveDarkTheme(darkTheme: Boolean) {
        repository.saveDarkTheme(darkTheme)
    }

}