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
import com.example.playlistmaker.ui.App

class SettingsViewModel(context: Context) : ViewModel() {

    companion object {
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY] as App)
                SettingsViewModel(app)
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
        //themeSettings.darkTheme = isDarkTheme
        settingsInteractor.updateThemeSetting(ThemeSettings(isDarkTheme))
        darkThemeLiveData.postValue(isDarkTheme)
    }

    fun shareApp(c: Context) {
        sharingInteractor.shareApp(c)
    }

    fun openTerms(c: Context) {
        sharingInteractor.openTerms(c)
    }

    fun openSupport(c: Context) {
        sharingInteractor.openSupport(c)
    }

}