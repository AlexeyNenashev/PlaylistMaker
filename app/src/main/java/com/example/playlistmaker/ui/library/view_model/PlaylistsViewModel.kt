package com.example.playlistmaker.ui.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.PlaylistInteractor
import com.example.playlistmaker.ui.library.PlaylistsState
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = stateLiveData

    //init {
    //    stateLiveData.postValue(PlaylistsState.NoPlaylists)
    //}

    fun showPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { playlists ->
                if(playlists.isEmpty()) {
                    stateLiveData.postValue(PlaylistsState.NoPlaylists)
                } else {
                    stateLiveData.postValue(PlaylistsState.Content(playlists))
                }
            }
        }
    }

}