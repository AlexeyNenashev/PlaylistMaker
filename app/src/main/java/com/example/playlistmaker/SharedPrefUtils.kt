package com.example.playlistmaker

import com.google.gson.Gson

val DARK_THEME_KEY = "dark_theme"
val HISTORY_KEY = "history"

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
    val json: String = sharedPrefs?.getString(HISTORY_KEY, null) ?: return emptyArray<Track>()
    return Gson().fromJson(json, Array<Track>::class.java)
}

fun saveSearchHistory() {
    val json: String = Gson().toJson(SearchHistory.items.toArray())
    sharedPrefs?.edit()
        ?.putString(HISTORY_KEY, json)
        ?.apply()
}
