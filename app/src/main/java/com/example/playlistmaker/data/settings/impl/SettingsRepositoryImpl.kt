package com.example.playlistmaker.data.settings.impl

import com.example.playlistmaker.data.search.StorageClient
import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.settings.model.ThemeSettings

class SettingsRepositoryImpl (private val storage: StorageClient<ThemeSettings>) :
    SettingsRepository {

    override fun getThemeSettings(): ThemeSettings? {
        return storage.getData()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        storage.storeData(settings)
    }


}