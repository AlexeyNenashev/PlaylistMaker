package com.example.playlistmaker.domain.api

interface PlayerRepository {
    fun play()
    fun pause()
    fun prepare()
    fun release()
}