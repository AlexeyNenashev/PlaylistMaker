package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(private val sharingInteractor: SharingInteractor,
                        private val settingsInteractor: SettingsInteractor) : ViewModel() {

    private var initialThemeSettings = settingsInteractor.getThemeSettings() ?: ThemeSettings(
        darkTheme = false
    )

    private val darkThemeLiveData = MutableLiveData<Boolean>(initialThemeSettings.darkTheme)
    fun observeDarkTheme(): LiveData<Boolean> = darkThemeLiveData

    fun rememberDarkTheme(isDarkTheme: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(isDarkTheme))
        darkThemeLiveData.postValue(isDarkTheme)
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

}