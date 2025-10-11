package com.example.playlistmaker.ui.search

import com.example.playlistmaker.domain.search.model.Track

sealed interface TracksState {

    object Loading : TracksState

    data class Content(
        val tracks: List<Track>
    ) : TracksState

    data class History(
        val tracks: List<Track>
    ) : TracksState

    object Error : TracksState

    object Empty : TracksState

}