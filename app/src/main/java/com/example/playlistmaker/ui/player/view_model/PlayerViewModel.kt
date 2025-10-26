package com.example.playlistmaker.ui.player.view_model

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.PlaylistInteractor
import com.example.playlistmaker.domain.library.SelectedTracksInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.library.PlaylistsState
import com.example.playlistmaker.ui.player.AddTrackToPlaylistResult
import com.example.playlistmaker.ui.player.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class PlayerViewModel(
    private val track: Track,
    private val mediaPlayer: MediaPlayer,
    private val selectedTracksInteractor: SelectedTracksInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

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

    private val playlistsLiveData = MutableLiveData<List<Playlist>>()
    fun observePlaylistsState(): LiveData<List<Playlist>> = playlistsLiveData

    private val addTrackLiveData = MutableLiveData<AddTrackToPlaylistResult>()
    fun observeAddTrack(): LiveData<AddTrackToPlaylistResult> = addTrackLiveData

    private var timerJob: Job? = null

    private val url = track.previewUrl
    private var playerMode = PlayerMode.PREPARED
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
        Log.d("playlists", "before: playerMode = $playerMode")
        when(playerMode) {
            PlayerMode.PLAYING -> pausePlayer()
            PlayerMode.PREPARED, PlayerMode.PAUSED -> startPlayer()
            PlayerMode.DEFAULT -> {}
        }
        Log.d("playlists", "after: playerMode = $playerMode")
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            if(track.isFavorite) {
                selectedTracksInteractor.deleteTrack(track)
            } else {
                selectedTracksInteractor.insertTrack(track)
            }
        }
        track.isFavorite = !track.isFavorite
        renderState()
    }

    private fun preparePlayer() {
        playerMode = PlayerMode.DEFAULT
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

    fun renderState() {
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
            track.country,
            track.isFavorite))
    }

    fun showPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { playlists ->
                playlistsLiveData.postValue(playlists)
            }
        }
    }

    fun addTrackToPlaylist(playlist: Playlist, position: Int) {
        if (track.trackId in playlist.trackIds) {
            addTrackLiveData.postValue(AddTrackToPlaylistResult.TrackWasAlreadyAdded(playlist.name))
        } else {
            viewModelScope.launch {
                playlistInteractor.addTrackToPlaylist(track, playlist).collect { updatedPlaylist ->
                    addTrackLiveData.postValue(AddTrackToPlaylistResult.Success(updatedPlaylist, position))
                }
            }
        }
    }

}