package com.example.playlistmaker.ui.playlistinfo.view_model

interface StringProvider {
    fun getQuantityString(pluralResId: Int, quantity: Int, vararg formatArgs: Any): String
}