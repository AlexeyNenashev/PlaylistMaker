package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson

const val PREFERENCES = "preferences"
const val DARK_THEME_KEY = "dark_theme"
const val HISTORY_KEY = "history"
val searchHistoryItems = ArrayList<Track>()

class App : Application() {

    var darkTheme = false
    //val searchHistoryItems = ArrayList<Track>()
    private var sharedPrefs: SharedPreferences? = null

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
        saveDarkTheme()
    }

    private fun isDarkTheme(): Boolean {
        return sharedPrefs?.getBoolean(DARK_THEME_KEY, false) ?: false
    }

    private fun saveDarkTheme() {
        sharedPrefs?.edit()
            ?.putBoolean(DARK_THEME_KEY, darkTheme)
            ?.apply()
    }

    private fun getSearchHistory(): Array<Track> {
        val json: String = sharedPrefs?.getString(HISTORY_KEY, null) ?: return emptyArray<Track>()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun saveSearchHistory() {
        val json: String = Gson().toJson(searchHistoryItems.toArray())
        sharedPrefs?.edit()
            ?.putString(HISTORY_KEY, json)
            ?.apply()
    }

}
