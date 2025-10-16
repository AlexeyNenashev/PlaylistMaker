package com.example.playlistmaker.domain.settings

import com.example.playlistmaker.domain.settings.model.ThemeSettings
import kotlinx.coroutines.flow.Flow

interface SettingsInteractor {
    fun getThemeSettings(): Flow<ThemeSettings?>
    suspend fun updateThemeSetting(settings: ThemeSettings)
}