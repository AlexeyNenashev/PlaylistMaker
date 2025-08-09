package com.example.playlistmaker.ui.search.view_model

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.search.TracksState
import com.example.playlistmaker.ui.App

class SearchViewModel(private val tracksInteractor: TracksInteractor,
                      private val historyInteractor: SearchHistoryInteractor,
                      private val context: Context): ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        //fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
        //    initializer {
        //        val app =
        //            (this[ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY] as App)
        //        SearchViewModel(app)
        //    }
        //}
    }

    //private val tracksInteractor = Creator.provideTracksInteractor()
    //private val historyInteractor = Creator.provideSearchHistoryInteractor(context)

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    private var latestSearchText: String? = null

    private val handler = Handler(Looper.getMainLooper())

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(
                TracksState.Loading
            )

            tracksInteractor.searchTracks(newSearchText, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                    handler.post {

                        val tracks = mutableListOf<Track>()
                        if (foundTracks != null) {
                            tracks.addAll(foundTracks)
                        }

                        when {
                            errorMessage != null -> {
                                renderState(
                                    TracksState.Error(
                                        errorMessage = context.getString(R.string.message_something_wrong),
                                    )
                                )

                           }

                            tracks.isEmpty() -> {
                                renderState(
                                    TracksState.Empty(
                                        message = context.getString(R.string.message_nothing_found),
                                    )
                                )
                            }

                            else -> {
                                renderState(
                                    TracksState.Content(
                                        tracks = tracks,
                                    )
                                )
                            }
                        }

                    }
                }
            })
        }
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun addToHistory(t: Track) {
        historyInteractor.saveToHistory(t)
    }

    fun clearHistory() {
        historyInteractor.clearHistory()
        renderState(
            TracksState.History(
                listOf<Track>()
            )
        )
    }

    fun showHistory() {
        historyInteractor.getHistory(object : SearchHistoryInteractor.HistoryConsumer {
            override fun consume(searchHistory: List<Track>?) {
                handler.post {
                    renderState(
                        TracksState.History(
                            searchHistory ?: listOf<Track>()
                        )
                    )
                }
            }
        })
    }

}