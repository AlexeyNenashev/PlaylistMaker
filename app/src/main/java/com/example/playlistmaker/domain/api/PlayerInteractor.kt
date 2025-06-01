package com.example.playlistmaker.domain.api


interface PlayerInteractor {
    fun play()
    fun pause()
    fun prepare(url: String, onPreparedListener: () -> Unit, onCompletionListener: () -> Unit)
    fun release()
    fun getCurrentPosition(): Int
}