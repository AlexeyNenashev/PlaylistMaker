package com.example.playlistmaker.ui.player.view_model

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.ui.player.PlayerState
import java.util.Locale

class PlayerViewModel(private val url: String) : ViewModel() {

    companion object {

        private const val COUNTER_DELAY = 500L
        private const val ZERO_TIME = "00:00"

        //fun getFactory(trackUrl: String): ViewModelProvider.Factory = viewModelFactory {
            //initializer {
            //    PlayerViewModel(trackUrl)
            //}
        //}

    }

    enum class PlayerMode {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED
    }



    private var playerMode = PlayerMode.DEFAULT
    private var progressTime = ZERO_TIME
    private val playerStateLiveData = MutableLiveData(PlayerState(false, progressTime))
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData


    private val mediaPlayer = MediaPlayer()

    private val handler = Handler(Looper.getMainLooper())

    private val timerRunnable = Runnable {
        if (playerMode == PlayerMode.PLAYING) {
            startTimerUpdate()
        }
    }

    init {
        preparePlayer()
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
        progressTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        renderState()
        handler.postDelayed(timerRunnable, COUNTER_DELAY)
    }

    private fun pauseTimer() {
        handler.removeCallbacks(timerRunnable)
    }

    private fun resetTimer() {
        handler.removeCallbacks(timerRunnable)
        progressTime = ZERO_TIME
        renderState()
    }

    fun onPause() {
        pausePlayer()
    }

    private fun renderState() {
        playerStateLiveData.postValue(PlayerState(playerMode == PlayerMode.PLAYING, progressTime))
    }

}