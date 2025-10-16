package com.example.playlistmaker.ui.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.SelectedTracksInteractor
import com.example.playlistmaker.ui.library.SelectedTracksState
import kotlinx.coroutines.launch

class SelectedTracksViewModel(private val selectedTracksInteractor: SelectedTracksInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<SelectedTracksState>()
    fun observeState(): LiveData<SelectedTracksState> = stateLiveData

    init {
        viewModelScope.launch {
            selectedTracksInteractor.getTracks().collect { tracks ->
                if(tracks.isEmpty()) {
                    stateLiveData.postValue(SelectedTracksState.NoTracks)
                } else {
                    tracks.forEach { track -> track.isFavorite = true }
                    stateLiveData.postValue(SelectedTracksState.Content(tracks))
                }
            }
        }

    }

}