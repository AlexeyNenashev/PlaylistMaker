package com.example.playlistmaker.ui.search.activity

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.player.activity.PlayerFragment
import com.example.playlistmaker.ui.search.TracksState
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {


    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        //const val EXTRA_TRACK = "EXTRA_TRACK"

        // Тег для использования во FragmentManager
        //const val TAG = "SearchFragment"
    }

    private val viewModel by viewModel<SearchViewModel>()

    private val trackAdapter = TrackAdapter {
        if (clickDebounce()) {
            viewModel.addToHistory(it)
            launchPlayerScreen(it)
        }
    }

    private val historyAdapter = TrackAdapter {
        if (clickDebounce()) {
            viewModel.addToHistory(it)
            launchPlayerScreen(it)
        }
    }

    private var textWatcher: TextWatcher? = null

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var binding: FragmentSearchBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //override fun onCreate(savedInstanceState: Bundle?) {
        //super.onCreate(savedInstanceState)
        //binding = ActivitySearchBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        //binding.toolbar.setNavigationOnClickListener {
        //    findNavController().navigateUp()
        //}

        binding.rvTrack.adapter = trackAdapter
        binding.historyTracks.adapter = historyAdapter

        viewModel.observeState().observe(viewLifecycleOwner) {
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
                viewModel.searchDebounce(s?.toString() ?: "")
            }
        }
        textWatcher?.let { binding.inputEditText.addTextChangedListener(it) }

        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            val inputMethodManager = requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.clearIcon.windowToken, 0)
        }

        binding.messageButton.setOnClickListener {
            if (clickDebounce()) {
                showOrHideMessage(Msg.HIDE)
                viewModel.searchRequest(binding.inputEditText.text.toString())
            }
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (binding.inputEditText.text.isEmpty() && hasFocus) {
                showHistoryIfItIsNotEmpty()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
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
        viewModel.showHistory()
    }

    private fun showOrHideMessage(msg: Msg) {
        when(msg) {
            Msg.NOTHING_FOUND -> {
                binding.apply {
                    messageIcon.setImageDrawable(getDrawable(requireContext(), R.drawable.nothing_found))
                    messageText.text = getString(R.string.message_nothing_found)
                    messageButton.visibility = View.GONE
                    messageLayout.visibility = View.VISIBLE
                    historyLayout.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
            }
            Msg.SOMETHING_WRONG -> {
                binding.apply {
                    messageIcon.setImageDrawable(getDrawable(requireContext(),R.drawable.something_wrong))
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

    fun launchPlayerScreen(t: Track) {
        findNavController().navigate(
            R.id.action_searchFragment_to_playerFragment,
            PlayerFragment.createArgs(t)
            )
    }

}