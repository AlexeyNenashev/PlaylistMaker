package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.settings.model.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings?
    fun updateThemeSetting(settings: ThemeSettings)
}