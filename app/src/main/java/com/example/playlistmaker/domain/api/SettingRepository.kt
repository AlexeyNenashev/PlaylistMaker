package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.settings.model.ThemeSettings

interface SettingRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}