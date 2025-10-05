package com.example.playlistmaker.ui.player.view_model

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.player.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class PlayerViewModel(private val track: Track, private val mediaPlayer: MediaPlayer) : ViewModel() {

    companion object {
        private const val UPDATE_TIME_INTERVAL = 300L
        private const val ZERO_TIME = "00:00"
    }

    enum class PlayerMode {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED
    }

    private var timerJob: Job? = null

    private val url = track.previewUrl
    private var playerMode = PlayerMode.DEFAULT
    private var progressTime = ZERO_TIME
    private val playerStateLiveData = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    init {
        preparePlayer()
        renderState()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        resetTimer()
    }

    fun onPlayButtonClicked() {
        when(playerMode) {
            PlayerMode.PLAYING -> pausePlayer()
            PlayerMode.PREPARED, PlayerMode.PAUSED -> startPlayer()
            PlayerMode.DEFAULT -> {}
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerMode = PlayerMode.PREPARED
            renderState()
        }
        mediaPlayer.setOnCompletionListener {
            playerMode = PlayerMode.PREPARED
            renderState()
            resetTimer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerMode = PlayerMode.PLAYING
        renderState()
        startTimerUpdate()
    }

    private fun pausePlayer() {
        pauseTimer()
        mediaPlayer.pause()
        playerMode = PlayerMode.PAUSED
        renderState()
    }

    private fun startTimerUpdate() {

        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                progressTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                renderState()
                delay(UPDATE_TIME_INTERVAL)
            }
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
    }

    private fun resetTimer() {
        timerJob?.cancel()
        progressTime = ZERO_TIME
        renderState()
    }

    fun onPause() {
        pausePlayer()
    }

    private fun renderState() {
        playerStateLiveData.postValue(PlayerState(
            playerMode == PlayerMode.PLAYING,
            progressTime,
            track.artworkUrlCover,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.collectionName,
            track.year,
            track.primaryGenreName,
            track.country))
    }

}