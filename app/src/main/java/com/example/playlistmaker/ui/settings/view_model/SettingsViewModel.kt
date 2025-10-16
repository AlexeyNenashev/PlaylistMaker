package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor
import kotlinx.coroutines.launch

class SettingsViewModel(private val sharingInteractor: SharingInteractor,
                        private val settingsInteractor: SettingsInteractor) : ViewModel() {

    private val darkThemeLiveData = MutableLiveData<Boolean>()
    fun observeDarkTheme(): LiveData<Boolean> = darkThemeLiveData

    init {
        var initialThemeSettings = ThemeSettings(darkTheme = false)
        viewModelScope.launch {
            settingsInteractor.getThemeSettings().collect { settings ->
                if (settings != null) {
                    initialThemeSettings = settings
                }
            }
            darkThemeLiveData.postValue(initialThemeSettings.darkTheme)
        }
    }

    fun rememberDarkTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            settingsInteractor.updateThemeSetting(ThemeSettings(isDarkTheme))
        }
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