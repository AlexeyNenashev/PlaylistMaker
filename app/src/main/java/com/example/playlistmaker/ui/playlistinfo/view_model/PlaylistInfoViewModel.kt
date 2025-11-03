package com.example.playlistmaker.ui.playlistinfo.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.library.PlaylistInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.ui.playlistinfo.PlaylistInfoState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlaylistInfoViewModel(
    private val playlistId: Int,
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor,
    private val stringProvider: StringProvider
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistInfoState>()
    fun observeState(): LiveData<PlaylistInfoState> = stateLiveData
    private var state: PlaylistInfoState? = null

    fun showPlaylistInfo() {
        viewModelScope.launch {
            showPlaylistInfoSuspend()
        }
    }

    fun deleteTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(track.trackId, playlistId)
            showPlaylistInfoSuspend()
        }
    }

    fun sharePlaylist() {
        if (state != null) {
            val howManyTracksString = stringProvider.getQuantityString(
                R.plurals.tracksCount,
                state?.howManyTracks ?: 0,
                state?.howManyTracks ?: 0
            )
            var textToShare = "${state?.name}\n${state?.description}\n$howManyTracksString"
            state?.tracks?.forEachIndexed { index, track ->
                textToShare += "\n${index + 1}. ${track.artistName} - ${track.trackName} (${track.trackTime})"
            }
            sharingInteractor.shareText(textToShare)
        }
    }

    suspend fun showPlaylistInfoSuspend() {
        playlistInteractor.getPlaylistAndItsTracksById(playlistId).collect { result ->
            val playlist = result.first
            val tracks = result.second
            val secondsInPlaylist: Int = tracks.sumOf {
                val minSec = it.trackTime.split(":")
                val min: Int = minSec[0].toInt()
                val sec: Int = minSec[1].toInt()
                min * 60 + sec
            }
            state = PlaylistInfoState(
                playlist.name,
                playlist.description,
                playlist.imageUri,
                secondsInPlaylist / 60,
                tracks.size,
                tracks
            )
            stateLiveData.postValue(state!!)
        }
    }

    fun deletePlaylist() {
        GlobalScope.launch {
            playlistInteractor.deletePlaylistById(playlistId)
        }
    }


}