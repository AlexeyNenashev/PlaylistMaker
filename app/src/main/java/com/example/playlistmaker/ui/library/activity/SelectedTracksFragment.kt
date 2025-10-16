package com.example.playlistmaker.ui.library.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSelectedTracksBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.library.SelectedTracksState
import com.example.playlistmaker.ui.library.view_model.SelectedTracksViewModel
import com.example.playlistmaker.ui.player.activity.PlayerFragment
import com.example.playlistmaker.ui.search.activity.TrackAdapter
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectedTracksFragment : Fragment() {

    private lateinit var onClickDebounce: (Track) -> Unit
    private val trackAdapter = TrackAdapter { onClickDebounce(it) }

    private var _binding: FragmentSelectedTracksBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SelectedTracksViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectedTracksBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvTrack.adapter = trackAdapter

        onClickDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
            launchPlayerScreen(track)
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is SelectedTracksState.NoTracks -> showNoTracks()
                is SelectedTracksState.Content -> showTracks(it.tracks)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSelectedTracks()
    }

    private fun showNoTracks() {
        binding.messageLayout.visibility = View.VISIBLE
        binding.rvTrack.visibility = View.GONE
    }

    private fun showTracks(tracks: List<Track>) {
        binding.messageLayout.visibility = View.GONE
        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(tracks)
        trackAdapter.notifyDataSetChanged()
        binding.rvTrack.visibility = View.VISIBLE
    }

    fun launchPlayerScreen(t: Track) {
        findNavController().navigate(
            R.id.action_libraryFragment_to_playerFragment,
            PlayerFragment.createArgs(t)
        )
    }

    companion object {
        fun newInstance() = SelectedTracksFragment()
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }
}