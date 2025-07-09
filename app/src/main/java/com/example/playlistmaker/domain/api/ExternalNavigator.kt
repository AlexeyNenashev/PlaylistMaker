package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.EmailData

interface ExternalNavigator {
    fun shareLink(url: String)
    fun openLink(url: String)
    fun openEmail(email: EmailData)
}