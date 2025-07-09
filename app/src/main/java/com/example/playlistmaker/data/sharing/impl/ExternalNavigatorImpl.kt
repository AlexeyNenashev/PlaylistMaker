package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorImpl(val context: Context) : ExternalNavigator {

    override fun shareLink(url: String) {
        val intent: Intent = Intent(Intent.ACTION_SEND).setType("text/plain").apply {
            putExtra(Intent.EXTRA_TEXT, url)
        }
        context.startActivity(intent)
    }

    override fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    override fun openEmail(email: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO).setData(Uri.parse("mailto:")).apply {
            putExtra(Intent.EXTRA_EMAIL, email.listOfEmails.toTypedArray())
            putExtra(Intent.EXTRA_SUBJECT, email.subject)
            putExtra(Intent.EXTRA_TEXT, email.text)
        }
        context.startActivity(intent)
    }

}