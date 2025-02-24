package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class SharedPrefUtils {

    companion object {

        private const val DARK_THEME_KEY = "dark_theme"
        private const val HISTORY_KEY = "history"

        var sharedPrefs: SharedPreferences? = null

        fun isDarkTheme(darkThemeDefaultValue: Boolean): Boolean {
            return if (sharedPrefs?.contains(DARK_THEME_KEY) == true) {
                sharedPrefs?.getBoolean(DARK_THEME_KEY, false) ?: false
            } else {
                darkThemeDefaultValue
            }
        }

        fun saveDarkTheme(darkTheme: Boolean) {
            sharedPrefs?.edit()
                ?.putBoolean(DARK_THEME_KEY, darkTheme)
                ?.apply()
        }

        fun getSearchHistory(): Array<Track> {
            val json: String =
                sharedPrefs?.getString(HISTORY_KEY, null) ?: return emptyArray<Track>()
            return Gson().fromJson(json, Array<Track>::class.java)
        }

        fun saveSearchHistory() {
            val json: String = Gson().toJson(SearchHistory.items.toArray())
            sharedPrefs?.edit()
                ?.putString(HISTORY_KEY, json)
                ?.apply()
        }

    }
}