package com.example.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
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
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.ui.player.AudioPlayerActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.SearchResult
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.TracksState
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {

    companion object {

        private const val CLICK_DEBOUNCE_DELAY = 1000L
        //private const val SEARCH_VALUE = "SEARCH_VALUE"
        const val EXTRA_TRACK = "EXTRA_TRACK"
    }







        //private const val SEARCH_DEBOUNCE_DELAY = 2000L
        //private var isClickAllowed = true
        //private var handler: Handler? = null

        //private val tracks = ArrayList<Track>()
        //private val history = ArrayList<Track>()
        //private lateinit var tracksInteractor: TracksInteractor
        //private lateinit var historyInteractor: HistoryInteractor
        //private lateinit var trackAdapter: TrackAdapter
        //private lateinit var historyAdapter: TrackAdapter


        //fun processClickOnSearchResult(track: Track, view: View, clickable: Boolean) {
        //    if (clickDebounce()) {
        //        if (clickable) {
        //            historyInteractor.update(history, track)
        //            historyInteractor.save(history)
        //        }
        //        val json: String = Gson().toJson(track)
        //        val displayIntent = Intent(view.context, AudioPlayerActivity::class.java)
        //        displayIntent.putExtra(EXTRA_TRACK, json)
        //        view.context.startActivity(displayIntent)
        //    }
        //}

        //private fun clickDebounce() : Boolean {
        //    val current = isClickAllowed
        //    if (isClickAllowed) {
        //        isClickAllowed = false
        //        handler?.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        //    }
        //    return current
        //}






    private var viewModel: SearchViewModel? = null
    private lateinit var binding: ActivitySearchBinding

    private val trackAdapter = TrackAdapter {
        if (clickDebounce()) {
            //historyInteractor.update(history, it) // !!!!!
            //historyInteractor.save(history)  // !!!!!
            val json: String = Gson().toJson(it)
            val displayIntent = Intent(this, AudioPlayerActivity::class.java)
            displayIntent.putExtra(EXTRA_TRACK, json)
            startActivity(displayIntent)
        }
    }

    private val historyAdapter = TrackAdapter {
        if (clickDebounce()) {
            //historyInteractor.update(history, it) // !!!!!
            //historyInteractor.save(history)  // !!!!!
            val json: String = Gson().toJson(it)
            val displayIntent = Intent(this, AudioPlayerActivity::class.java)
            displayIntent.putExtra(EXTRA_TRACK, json)
            startActivity(displayIntent)
        }
    }

    private var textWatcher: TextWatcher? = null

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
        //    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        //    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        //    insets
        //}

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.rvTrack.adapter = trackAdapter
        binding.historyTracks.adapter = historyAdapter

        viewModel = ViewModelProvider(this, SearchViewModel.getFactory())
            .get(SearchViewModel::class.java)

        viewModel?.observeState()?.observe(this) {
            render(it)
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable?) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                if (s.isNullOrEmpty() && binding.inputEditText.hasFocus()) {
                    showHistoryIfItIsNotEmpty()
                }
                viewModel?.searchDebounce(s?.toString() ?: "")
            }
        }
        textWatcher?.let { binding.inputEditText.addTextChangedListener(it) }











        //tracksInteractor = Creator.provideTracksInteractor()
        //historyInteractor = Creator.provideHistoryInteractor()
        //trackAdapter = TrackAdapter(tracks, true, history, historyInteractor)
        //historyAdapter = TrackAdapter(history, false, history, historyInteractor)
        //historyInteractor.read(history)

        //val inputEditText = findViewById<EditText>(R.id.inputEditText)
        //val clearButton = findViewById<ImageView>(R.id.clearIcon)
        //val refreshButton = findViewById<Button>(R.id.messageButton)
        //val clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)

        //handler = Handler(Looper.getMainLooper())
        //isClickAllowed = true

        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.clearIcon.windowToken, 0)
        }

        binding.messageButton.setOnClickListener {
            showOrHideMessage(Msg.HIDE)
            viewModel?.searchDebounce(binding.inputEditText.text.toString())   // clickDebounce()  ???
        }

        binding.clearHistoryButton.setOnClickListener {
            historyAdapter.tracks.clear()
            //historyInteractor.save(history)  // !!!!!!!
            showOrHideMessage(Msg.HIDE)
        }

        //binding.inputEditText.doOnTextChanged { s, start, before, count ->
        //    binding.clearIcon.visibility = clearButtonVisibility(s)
        //    searchValue = s.toString()
        //    if (searchValue.isEmpty() && binding.inputEditText.hasFocus()) {
        //        showHistoryIfItIsNotEmpty()
        //    }
        //    searchDebounce()
        //}

        binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (binding.inputEditText.text.isEmpty() && hasFocus) {
                showHistoryIfItIsNotEmpty()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { binding.inputEditText.removeTextChangedListener(it) }
    }


    //override fun onSaveInstanceState(outState: Bundle) {
    //    super.onSaveInstanceState(outState)
    //    outState.putString(SEARCH_VALUE, searchValue)
    //}

    //override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    //    super.onRestoreInstanceState(savedInstanceState)
    //    searchValue = savedInstanceState.getString(SEARCH_VALUE, "")
    //    val inputEditText = findViewById<EditText>(R.id.inputEditText)
    //    inputEditText.setText(searchValue)
    //}







    //private lateinit var tracksInteractor: TracksInteractor
    //private lateinit var historyInteractor: HistoryInteractor
    //private val tracks = ArrayList<Track>()
    //private val history = ArrayList<Track>()


    //private var searchValue = ""

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }







    //private fun makeSearch() {
    //    if (searchValue.isNotEmpty()) {
    //        tracks.clear()
    //        trackAdapter.notifyDataSetChanged()
    //        showOrHideMessage(Msg.PROGRESS)
    //
    //        tracksInteractor.searchTracks(searchValue,
    //            object : TracksInteractor.TracksConsumer {
    //                override fun consume(searchResult: SearchResult) {
    //                    handler?.post {
    //                        if (!searchResult.success) {
    //                            showOrHideMessage(Msg.SOMETHING_WRONG)
    //                        }
    //                        else if (searchResult.tracks.isEmpty()) {
    //                            showOrHideMessage(Msg.NOTHING_FOUND)
    //                        } else {
    //                            tracks.addAll(searchResult.tracks)
    //                            trackAdapter.notifyDataSetChanged()
    //                            showOrHideMessage(Msg.HIDE)
    //                        }
    //                    }
    //                }
    //            })
    //    }
    //}

    private enum class Msg{
        NOTHING_FOUND,
        SOMETHING_WRONG,
        HIDE,
        HISTORY,
        PROGRESS;
    }

    private fun showHistoryIfItIsNotEmpty() {
        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()
        showOrHideMessage(if (historyAdapter.tracks.isEmpty()) Msg.HIDE else Msg.HISTORY)
    }

    private fun showOrHideMessage(msg: Msg) {
        //val layout = findViewById<View>(R.id.messageLayout)
        //val icon = findViewById<ImageView>(R.id.messageIcon)
        //val text = findViewById<TextView>(R.id.messageText)
        //val button = findViewById<Button>(R.id.messageButton)
        //val history = findViewById<View>(R.id.historyLayout)
        //val progress = findViewById<ProgressBar>(R.id.progressBar)
        when(msg) {
            Msg.NOTHING_FOUND -> {
                binding.apply {
                    messageIcon.setImageDrawable(getDrawable(R.drawable.nothing_found))
                    messageText.text = getString(R.string.message_nothing_found)
                    messageButton.visibility = View.GONE
                    messageLayout.visibility = View.VISIBLE
                    historyLayout.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
            }
            Msg.SOMETHING_WRONG -> {
                binding.apply {
                    messageIcon.setImageDrawable(getDrawable(R.drawable.something_wrong))
                    messageText.text = getString(R.string.message_something_wrong)
                    messageButton.visibility = View.VISIBLE
                    messageLayout.visibility = View.VISIBLE
                    historyLayout.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
            }
            Msg.HIDE -> {
                binding.apply {
                    messageLayout.visibility = View.GONE
                    historyLayout.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
            }
            Msg.HISTORY -> {
                historyAdapter.notifyDataSetChanged()
                binding.apply {
                    messageLayout.visibility = View.GONE
                    historyLayout.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
            Msg.PROGRESS -> {
                binding.apply {
                    messageLayout.visibility = View.GONE
                    historyLayout.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
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

    //private val searchRunnable = Runnable { makeSearch() }

    //private fun searchDebounce() {
    //    handler?.removeCallbacks(searchRunnable)
    //    handler?.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    //}

    fun render(state: TracksState) {
        when (state) {
            is TracksState.Loading -> showOrHideMessage(Msg.PROGRESS)
            is TracksState.Error -> showOrHideMessage(Msg.SOMETHING_WRONG)   //(state.errorMessage)
            is TracksState.Empty -> showOrHideMessage(Msg.NOTHING_FOUND)  //(state.message)
            is TracksState.Content -> {
                trackAdapter.tracks.clear()
                trackAdapter.tracks.addAll(state.tracks)
                trackAdapter.notifyDataSetChanged()
                showOrHideMessage(Msg.HIDE)
            }
            is TracksState.History -> {
                historyAdapter.tracks.clear()
                historyAdapter.tracks.addAll(state.tracks)
                //historyAdapter.notifyDataSetChanged()
                showOrHideMessage(Msg.HISTORY)
            }
        }
    }


}
