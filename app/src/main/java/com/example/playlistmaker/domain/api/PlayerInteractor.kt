package com.example.playlistmaker.domain.api

interface PlayerInteractor {
    fun play()
    fun pause()
    fun prepare()
    fun release()
}