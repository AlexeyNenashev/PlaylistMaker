package com.example.playlistmaker.ui.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.ui.library.PlaylistsState

class PlaylistsViewModel() : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = stateLiveData

    init {
        stateLiveData.postValue(PlaylistsState.NoPlaylists)
    }

}