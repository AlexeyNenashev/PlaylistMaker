package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val PREFERENCES = "preferences"
val searchHistoryItems = ArrayList<Track>()
var sharedPrefs: SharedPreferences? = null

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        switchTheme(isDarkTheme())
        searchHistoryItems.clear()
        searchHistoryItems += getSearchHistory()
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
        saveDarkTheme(darkTheme)
    }

}
