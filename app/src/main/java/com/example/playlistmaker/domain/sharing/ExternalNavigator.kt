package com.example.playlistmaker.domain.sharing

import com.example.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {
    fun shareText(text: String)
    fun openLink(url: String)
    fun openEmail(email: EmailData)
}