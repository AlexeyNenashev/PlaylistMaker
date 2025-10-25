package com.example.playlistmaker.ui.createplaylist.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.CreatePlaylistInteractor
import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(private val createPlaylistInteractor: CreatePlaylistInteractor) : ViewModel() {

    fun createPlaylist(
        playlistName: String,
        playlistDescription: String,
        imageUri: Uri?
    ) {
        val savedImageFilePath = savePicture(imageUri)
        val playlist = Playlist(
            name = playlistName,
            description = playlistDescription,
            imageUri = savedImageFilePath,
            trackIds = emptyList()
        )
        viewModelScope.launch { createPlaylistInteractor.createPlaylist(playlist) }
    }

    fun savePicture(imageUri: Uri?): String {
        if (imageUri == null) {
            return ""
        }
                return ""
    }

}