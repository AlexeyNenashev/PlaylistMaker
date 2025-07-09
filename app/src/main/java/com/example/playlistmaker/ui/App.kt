package com.example.playlistmaker.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import android.content.res.Configuration
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings

class App : Application() {

    //var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        //sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        //darkThemeInteractor = Creator.provideDarkThemeInteractor()
        val settingsInteractor: SettingsInteractor = Creator.provideSettingsInteractor(this)
        val darkTheme = (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES)
        val themeSettingsDefault = ThemeSettings(darkTheme)
        val themeSettings = settingsInteractor.getThemeSettings() ?: themeSettingsDefault
        switchTheme(themeSettings.darkTheme)
        settingsInteractor.updateThemeSetting(themeSettings)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        //darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        //darkThemeInteractor.saveDarkTheme(darkTheme)
    }

    //companion object {
        //private const val PREFERENCES = "preferences"
        //lateinit var sharedPrefs: SharedPreferences
        //lateinit var darkThemeInteractor: DarkThemeInteractor
    //}


}
