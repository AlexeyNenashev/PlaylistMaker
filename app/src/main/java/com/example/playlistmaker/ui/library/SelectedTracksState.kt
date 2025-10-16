package com.example.playlistmaker.ui.library

import com.example.playlistmaker.domain.model.Track

sealed interface SelectedTracksState {

    object NoTracks : SelectedTracksState

    data class Content(
        val tracks: List<Track>
    ) : SelectedTracksState

}