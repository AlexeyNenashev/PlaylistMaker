package com.example.playlistmaker.data.storage

import com.example.playlistmaker.data.StorageClient
import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.models.ThemeSettings

class SettingsRepositoryImpl (private val storage: StorageClient<ThemeSettings>) : SettingsRepository {

    override fun getThemeSettings(): ThemeSettings? {
        return storage.getData()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        storage.storeData(settings)
    }


}