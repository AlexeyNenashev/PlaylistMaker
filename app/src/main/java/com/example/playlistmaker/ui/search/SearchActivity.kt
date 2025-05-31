package com.example.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.ui.player.AudioPlayerActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.HistoryInteractor
//import com.example.playlistmaker.presentation.SearchHistory
//import com.example.playlistmaker.SharedPrefUtils
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {

    private val tracks = ArrayList<Track>()
    private val history = ArrayList<Track>()
    private lateinit var tracksInteractor: TracksInteractor //= Creator.provideTracksInteractor()
    private lateinit var historyInteractor: HistoryInteractor //= Creator.provideHistoryInteractor(this)
    private lateinit var trackAdapter: TrackAdapter //= TrackAdapter(tracks, true, history, historyInteractor)
    private lateinit var historyAdapter: TrackAdapter //= TrackAdapter(history, false, history, historyInteractor)
    private var searchValue = ""
    //private val iTunesService = RetrofitClient().getITunesService()
    //private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        tracksInteractor = Creator.provideTracksInteractor()
        historyInteractor = Creator.provideHistoryInteractor(this)
        trackAdapter = TrackAdapter(tracks, true, history, historyInteractor)
        historyAdapter = TrackAdapter(history, false, history, historyInteractor)

        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val refreshButton = findViewById<Button>(R.id.messageButton)
        val clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)

        val rvTrack = findViewById<RecyclerView>(R.id.rvTrack)
        rvTrack.adapter = trackAdapter

        val historyTracks = findViewById<RecyclerView>(R.id.historyTracks)
        historyTracks.adapter = historyAdapter

        handler = Handler(Looper.getMainLooper())
        isClickAllowed = true

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
        }

        refreshButton.setOnClickListener {
            showOrHideMessage(Msg.HIDE)
            makeSearch()
        }

        clearHistoryButton.setOnClickListener {
            history.clear()
            historyInteractor.save(history)
            showOrHideMessage(Msg.HIDE)
        }

        inputEditText.doOnTextChanged { s, start, before, count ->
            clearButton.visibility = clearButtonVisibility(s)
            searchValue = s.toString()
            if (searchValue.isEmpty() && inputEditText.hasFocus()) {
                showHistoryIfItIsNotEmpty()
            }
            searchDebounce()
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (inputEditText.text.isEmpty() && hasFocus) {
                showHistoryIfItIsNotEmpty()
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE, searchValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchValue = savedInstanceState.getString(SEARCH_VALUE, "")
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        inputEditText.setText(searchValue)
    }

    private fun makeSearch() {
        if (searchValue.isNotEmpty()) {
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            showOrHideMessage(Msg.PROGRESS)

            tracksInteractor.searchTracks(searchValue,
                object : TracksInteractor.TracksConsumer {
                    override fun consume(foundTracks: List<Track>) {
                        handler?.post {
                            if (foundTracks.isEmpty()) {
                                showOrHideMessage(Msg.NOTHING_FOUND)
                            } else {
                                tracks.addAll(foundTracks)
                                trackAdapter.notifyDataSetChanged()
                                showOrHideMessage(Msg.HIDE)
                            }
                            //TODO("showOrHideMessage(Msg.SOMETHING_WRONG)")
                        }
                    }
                })





        //    iTunesService.search(searchValue).enqueue(object :
        //        Callback<TracksSearchResponse> {
        //        override fun onResponse(call: Call<TracksSearchResponse>,
        //                                response: Response<TracksSearchResponse>
        //        ) {
        //            if (response.code() == 200) {
        //                tracks.clear()
        //                if (response.body()?.results?.isNotEmpty() == true) {
        //                    tracks.addAll(response.body()?.results ?: emptyList<Track>())
        //                }
        //                trackAdapter.notifyDataSetChanged()
        //
        //            } else {
        //                showOrHideMessage(Msg.SOMETHING_WRONG)
        //            }
        //        }
        //
        //        override fun onFailure(call: Call<TracksSearchResponse>, t: Throwable) {
        //            showOrHideMessage(Msg.SOMETHING_WRONG)
        //        }
        //
        //    })
        }
    }

    private enum class Msg{
        NOTHING_FOUND,
        SOMETHING_WRONG,
        HIDE,
        HISTORY,
        PROGRESS;
    }

    private fun showHistoryIfItIsNotEmpty() {
        tracks.clear()
        trackAdapter.notifyDataSetChanged()
        showOrHideMessage(if (history.isEmpty()) Msg.HIDE else Msg.HISTORY)
    }

    private fun showOrHideMessage(msg: Msg) {
        val layout = findViewById<View>(R.id.messageLayout)
        val icon = findViewById<ImageView>(R.id.messageIcon)
        val text = findViewById<TextView>(R.id.messageText)
        val button = findViewById<Button>(R.id.messageButton)
        val history = findViewById<View>(R.id.historyLayout)
        val progress = findViewById<ProgressBar>(R.id.progressBar)
        when(msg) {
            Msg.NOTHING_FOUND -> {
                icon.setImageDrawable(getDrawable(R.drawable.nothing_found))
                text.text = getString(R.string.message_nothing_found)
                button.visibility = View.GONE
                layout.visibility = View.VISIBLE
                history.visibility = View.GONE
                progress.visibility = View.GONE
            }
            Msg.SOMETHING_WRONG -> {
                icon.setImageDrawable(getDrawable(R.drawable.something_wrong))
                text.text = getString(R.string.message_something_wrong)
                button.visibility = View.VISIBLE
                layout.visibility = View.VISIBLE
                history.visibility = View.GONE
                progress.visibility = View.GONE
            }
            Msg.HIDE -> {
                layout.visibility = View.GONE
                history.visibility = View.GONE
                progress.visibility = View.GONE
            }
            Msg.HISTORY -> {
                historyAdapter.notifyDataSetChanged()
                layout.visibility = View.GONE
                history.visibility = View.VISIBLE
                progress.visibility = View.GONE
            }
            Msg.PROGRESS -> {
                layout.visibility = View.GONE
                history.visibility = View.GONE
                progress.visibility = View.VISIBLE
            }
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private val searchRunnable = Runnable { makeSearch() }

    private fun searchDebounce() {
        handler?.removeCallbacks(searchRunnable)
        handler?.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {

        private const val SEARCH_VALUE = "SEARCH_VALUE"
        const val EXTRA_TRACK = "EXTRA_TRACK"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private var isClickAllowed = true
        private var handler: Handler? = null

        fun processClickOnSearchResult(track: Track, view: View, clickable: Boolean, history: ArrayList<Track>, historyInteractor: HistoryInteractor) {
            if (clickDebounce()) {
                if (clickable) {
                    historyInteractor.update(history, track)
                    historyInteractor.save(history)
                    //Toast.makeText(this@Companion, "save history", Toast.LENGTH_SHORT).show()
                }
                val json: String = Gson().toJson(track)
                val displayIntent = Intent(view.context, AudioPlayerActivity::class.java)
                displayIntent.putExtra(EXTRA_TRACK, json)
                view.context.startActivity(displayIntent)
            }
        }

        private fun clickDebounce() : Boolean {
            val current = isClickAllowed
            if (isClickAllowed) {
                isClickAllowed = false
                handler?.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
            }
            return current
        }

    }

}
