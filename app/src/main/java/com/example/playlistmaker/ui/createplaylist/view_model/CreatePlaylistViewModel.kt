package com.example.playlistmaker.ui.createplaylist.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.PlaylistInteractor
import com.example.playlistmaker.domain.library.SavePictureUseCase
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.createplaylist.PlaylistStateForEditing
import com.example.playlistmaker.ui.playlistinfo.PlaylistInfoState
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val playlistId: Int?,
    private val playlistInteractor: PlaylistInteractor,
    private val savePictureUseCase : SavePictureUseCase
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistStateForEditing>()
    fun observeState(): LiveData<PlaylistStateForEditing> = stateLiveData
    private var initialPlaylist: Playlist? = null

    init {
        if (playlistId != null) {
            viewModelScope.launch {
                playlistInteractor.getPlaylistAndItsTracksById(playlistId, false).collect { result ->
                    initialPlaylist = result.first
                    stateLiveData.postValue(
                        PlaylistStateForEditing(
                            initialPlaylist?.name ?: "",
                            initialPlaylist?.description ?: "",
                            initialPlaylist?.imageUri ?: ""
                        )
                    )
                }
            }
        }
    }

    fun createOrUpdatePlaylist(
        playlistName: String,
        playlistDescription: String,
        imageUri: Uri?
    ) {
        val savedImageFilePath = savePictureUseCase.saveOrUpdatePicture(imageUri, initialPlaylist?.imageUri ?: "")
        val playlist = Playlist(
            id = playlistId ?: 0,
            name = playlistName,
            description = playlistDescription,
            imageUri = savedImageFilePath,
            trackIds = initialPlaylist?.trackIds ?: emptyList()
        )
        viewModelScope.launch {
            if (playlistId == null) { playlistInteractor.createPlaylist(playlist) }
            else { playlistInteractor.updatePlaylist(playlist) }
        }
    }

}