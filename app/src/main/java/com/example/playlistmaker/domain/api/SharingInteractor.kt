package com.example.playlistmaker.domain.api

import android.content.Context

interface SharingInteractor {
    fun shareApp(c: Context)
    fun openTerms(c: Context)
    fun openSupport(c: Context)
}