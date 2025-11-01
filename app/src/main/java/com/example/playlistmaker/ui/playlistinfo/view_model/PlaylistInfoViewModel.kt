package com.example.playlistmaker.ui.playlistinfo.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.PlaylistInteractor
import com.example.playlistmaker.ui.playlistinfo.PlaylistInfoState
import kotlinx.coroutines.launch

class PlaylistInfoViewModel(
    private val playlistId: Int,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistInfoState>()
    fun observeState(): LiveData<PlaylistInfoState> = stateLiveData

    fun showPlaylistInfo() {
        viewModelScope.launch {
            playlistInteractor.getPlaylistAndItsTracksById(playlistId).collect { result ->
                val playlist = result.first
                val tracks = result.second
                val secondsInPlaylist: Int = tracks.sumOf {
                    val minSec = it.trackTime.split(":")
                    val min: Int = minSec[0].toInt()
                    val sec: Int = minSec[1].toInt()
                    min * 60 + sec
                }
                stateLiveData.postValue(PlaylistInfoState(
                    playlist.name,
                    playlist.description,
                    playlist.imageUri,
                    secondsInPlaylist / 60,
                    tracks.size,
                    tracks
                ))
            }
        }
    }

}