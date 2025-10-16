package com.example.playlistmaker.data.settings.impl

import com.example.playlistmaker.data.search.StorageClient
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SettingsRepositoryImpl (private val storage: StorageClient<ThemeSettings>) :
    SettingsRepository {

    override fun getThemeSettings(): Flow<ThemeSettings?> = flow {
        emit(storage.getData())
    }

    override suspend fun updateThemeSetting(settings: ThemeSettings) {
        storage.storeData(settings)
    }


}