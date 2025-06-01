package com.example.playlistmaker.domain.api

import android.media.MediaPlayer

interface PlayerInteractor {
    fun play()
    fun pause()
    fun prepare(url: String, onPreparedListener: (MediaPlayer) -> Unit, onCompletionListener: (MediaPlayer) -> Unit)
    fun release()
    fun getCurrentPosition(): Int
}