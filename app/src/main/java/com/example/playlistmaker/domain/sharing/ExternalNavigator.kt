package com.example.playlistmaker.domain.sharing

import android.content.Context
import com.example.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {
    fun shareLink(url: String, c: Context)
    fun openLink(url: String, c: Context)
    fun openEmail(email: EmailData, c: Context)
}