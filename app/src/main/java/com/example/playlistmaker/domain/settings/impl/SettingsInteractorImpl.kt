package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import kotlinx.coroutines.flow.Flow

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {

    override fun getThemeSettings(): Flow<ThemeSettings?> {
        return repository.getThemeSettings()
    }

    override suspend fun updateThemeSetting(settings: ThemeSettings) {
        repository.updateThemeSetting(settings)
    }

}