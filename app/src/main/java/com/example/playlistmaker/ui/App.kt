package com.example.playlistmaker.ui

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import android.content.res.Configuration
import com.example.playlistmaker.Creator
//import com.example.playlistmaker.SharedPrefUtils
import com.example.playlistmaker.domain.api.DarkThemeInteractor
//import com.example.playlistmaker.presentation.SearchHistory

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        darkThemeInteractor = Creator.provideDarkThemeInteractor()
        darkTheme = (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES)
        switchTheme(darkThemeInteractor.isDarkTheme(darkTheme))
        //SearchHistory.read()
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
        darkThemeInteractor.saveDarkTheme(darkTheme)
    }

    companion object {
        private const val PREFERENCES = "preferences"
        lateinit var sharedPrefs: SharedPreferences
        lateinit var darkThemeInteractor: DarkThemeInteractor
    }


}
