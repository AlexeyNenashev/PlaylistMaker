package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import android.content.res.Configuration

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        SharedPrefUtils.sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        darkTheme = (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES)
        switchTheme(SharedPrefUtils.isDarkTheme(darkTheme))
        SearchHistory.read()
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
        SharedPrefUtils.saveDarkTheme(darkTheme)
    }

    companion object {
        const val PREFERENCES = "preferences"
    }

}
