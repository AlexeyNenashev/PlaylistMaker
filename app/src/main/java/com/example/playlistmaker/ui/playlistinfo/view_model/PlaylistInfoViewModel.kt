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
            //val howManyTracksString = numberToString(state?.howManyTracks ?: 0, "треков", "трек", "трека")
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
        viewModelScope.launch {
            playlistInteractor.deletePlaylistById(playlistId)
        }
    }

    //private fun numberToString(number: Int, string0: String, string1: String, string2: String): String {
    //    val n10  = number % 10
    //    val n100 = number % 100
    //    var s = "$number $string0"
    //    if (n10 == 1 && n100 != 11) { s = "$number $string1" }
    //    if (n10 == 2 && n100 != 12) { s = "$number $string2" }
    //    if (n10 == 3 && n100 != 13) { s = "$number $string2" }
    //    if (n10 == 4 && n100 != 14) { s = "$number $string2" }
    //    return s
    //}

}