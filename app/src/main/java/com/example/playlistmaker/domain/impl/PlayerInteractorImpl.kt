package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository


class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {

    override fun play() {
        repository.play()
    }

    override fun pause() {
        repository.pause()
    }

    override fun prepare(url: String, onPreparedListener: () -> Unit, onCompletionListener: () -> Unit) {
        repository.prepare(url, onPreparedListener, onCompletionListener)
    }

    override fun release() {
        repository.release()
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }

}