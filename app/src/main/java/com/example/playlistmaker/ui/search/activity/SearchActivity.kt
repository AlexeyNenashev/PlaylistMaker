package com.example.playlistmaker.ui.search.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.ui.search.TracksState
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        const val EXTRA_TRACK = "EXTRA_TRACK"
    }

    private var viewModel: SearchViewModel? = null
    private lateinit var binding: ActivitySearchBinding

    private val trackAdapter = TrackAdapter {
        if (clickDebounce()) {
            viewModel?.addToHistory(it)
            val json: String = Gson().toJson(it)
            val displayIntent = Intent(this, PlayerActivity::class.java)
            displayIntent.putExtra(EXTRA_TRACK, json)
            startActivity(displayIntent)
        }
    }

    private val historyAdapter = TrackAdapter {
        if (clickDebounce()) {
            viewModel?.addToHistory(it)
            val json: String = Gson().toJson(it)
            val displayIntent = Intent(this, PlayerActivity::class.java)
            displayIntent.putExtra(EXTRA_TRACK, json)
            startActivity(displayIntent)
        }
    }

    private var textWatcher: TextWatcher? = null

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.rvTrack.adapter = trackAdapter
        binding.historyTracks.adapter = historyAdapter

        viewModel = ViewModelProvider(this, SearchViewModel.Companion.getFactory())
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

        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.clearIcon.windowToken, 0)
        }

        binding.messageButton.setOnClickListener {
            if (clickDebounce()) {
                showOrHideMessage(Msg.HIDE)
                viewModel?.searchRequest(binding.inputEditText.text.toString())
            }
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel?.clearHistory()
        }

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

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

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
        viewModel?.showHistory()
    }

    private fun showOrHideMessage(msg: Msg) {
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

    fun render(state: TracksState) {
        when (state) {
            is TracksState.Loading -> {
                trackAdapter.tracks.clear()
                trackAdapter.notifyDataSetChanged()
                showOrHideMessage(Msg.PROGRESS)
            }
            is TracksState.Error -> showOrHideMessage(Msg.SOMETHING_WRONG)
            is TracksState.Empty -> showOrHideMessage(Msg.NOTHING_FOUND)
            is TracksState.Content -> {
                trackAdapter.tracks.clear()
                trackAdapter.tracks.addAll(state.tracks)
                trackAdapter.notifyDataSetChanged()
                showOrHideMessage(Msg.HIDE)
            }
            is TracksState.History -> {
                historyAdapter.tracks.clear()
                historyAdapter.tracks.addAll(state.tracks)
                showOrHideMessage(if (historyAdapter.tracks.isEmpty()) Msg.HIDE else Msg.HISTORY)
            }
        }
    }

}