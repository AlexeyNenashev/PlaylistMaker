package com.example.playlistmaker.ui.settings.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(context: Context) : ViewModel() {

    companion object {
        fun getFactory(contextFromActivity: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(contextFromActivity)
            }
        }
    }

    private val sharingInteractor: SharingInteractor = Creator.provideSharingInteractor(context)
    private val settingsInteractor: SettingsInteractor = Creator.provideSettingsInteractor(context)

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