package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val PREFERENCES = "preferences"
const val DARK_THEME_KEY = "dark_theme"

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        switchTheme(sharedPrefs.getBoolean(DARK_THEME_KEY, false))
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
