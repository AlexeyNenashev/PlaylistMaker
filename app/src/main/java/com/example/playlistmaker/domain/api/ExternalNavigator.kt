package com.example.playlistmaker.domain.api

import android.content.Context
import com.example.playlistmaker.domain.models.EmailData

interface ExternalNavigator {
    fun shareLink(url: String, c: Context)
    fun openLink(url: String, c: Context)
    fun openEmail(email: EmailData, c: Context)
}