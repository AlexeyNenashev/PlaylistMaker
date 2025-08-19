package com.example.playlistmaker.ui.library

sealed interface SelectedTracksState {
    object NoTracks : SelectedTracksState
}