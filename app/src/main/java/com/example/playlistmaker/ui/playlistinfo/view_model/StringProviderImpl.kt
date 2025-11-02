package com.example.playlistmaker.ui.playlistinfo.view_model

import android.content.Context

class StringProviderImpl(private val context: Context) : StringProvider {
    override fun getQuantityString(pluralResId: Int, quantity: Int, vararg formatArgs: Any): String {
        return context.resources.getQuantityString(pluralResId, quantity, *formatArgs)
    }
}