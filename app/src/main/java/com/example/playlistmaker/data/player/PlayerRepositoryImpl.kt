package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.PlayerRepository

class PlayerRepositoryImpl : PlayerRepository {

    private val mediaPlayer = MediaPlayer()

    override fun play() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun prepare(url: String, onPreparedListener: () -> Unit, onCompletionListener: () -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener { onPreparedListener() }
        mediaPlayer.setOnCompletionListener { onCompletionListener() }
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

}