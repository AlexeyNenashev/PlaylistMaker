package com.example.playlistmaker.ui.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.ui.library.SelectedTracksState

class SelectedTracksViewModel() : ViewModel() {

    private val stateLiveData = MutableLiveData<SelectedTracksState>()
    fun observeState(): LiveData<SelectedTracksState> = stateLiveData

    init {
        stateLiveData.postValue(SelectedTracksState.NoTracks)
    }

}