package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.ExternalNavigator
import com.example.playlistmaker.domain.api.SharingInteractor
import com.example.playlistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val context: Context
) : SharingInteractor {

    override fun shareApp(c: Context) {
        externalNavigator.shareLink(getShareAppLink(), c)
    }

    override fun openTerms(c: Context) {
        externalNavigator.openLink(getTermsLink(), c)
    }

    override fun openSupport(c: Context) {
        externalNavigator.openEmail(getSupportEmailData(), c)
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.linkToCourse)
    }

    private fun getSupportEmailData(): EmailData {
        val email = context.getString(R.string.myEmail)
        val subject = context.getString(R.string.emailSubject)
        val text = context.getString(R.string.emailText)
        return EmailData(listOf(email), subject, text)
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.practicumOffer)
    }

}