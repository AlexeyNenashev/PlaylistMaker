package com.example.playlistmaker.ui.playlistinfo.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.createplaylist.activity.CreatePlaylistFragment
import com.example.playlistmaker.ui.player.activity.PlayerFragment
import com.example.playlistmaker.ui.playlistinfo.PlaylistInfoState
import com.example.playlistmaker.ui.playlistinfo.view_model.PlaylistInfoViewModel
import com.example.playlistmaker.ui.search.activity.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.core.parameter.parametersOf
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistInfoFragment : Fragment() {

    companion object {

        private const val ARGS_PLAYLIST_ID = "playlist_id"

        fun createArgs(playlistId: Int): Bundle =
            bundleOf(ARGS_PLAYLIST_ID to playlistId)

    }

    private var playlistId: Int? = null
    private var howManyTracks = 0
    private var playlistName = ""
    private val viewModel by viewModel<PlaylistInfoViewModel> { parametersOf(playlistId) }
    private val trackAdapter = TrackAdapter({ launchPlayerScreen(it) }, { deleteTrackDialog(it) })
    private var _binding: FragmentPlaylistInfoBinding? = null
    private val binding get() = _binding!!
    lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPlaylistInfoBinding.inflate(inflater, container, false)
        playlistId = requireArguments().getInt(ARGS_PLAYLIST_ID)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet2)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.shareButton.setOnClickListener { sharePlaylist() }
        binding.shareInBottomSheet.setOnClickListener { sharePlaylist() }
        binding.deleteInBottomSheet.setOnClickListener { deletePlaylist() }
        binding.editInBottomSheet.setOnClickListener { editPlaylist() }
        binding.menuButton.setOnClickListener { bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED }
        binding.recyclerView.adapter = trackAdapter
        binding.arrowBack.setOnClickListener { findNavController().navigateUp() }
        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }
        viewModel.showPlaylistInfo()

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                var alpha = slideOffset + 1f
                if (alpha > 1f) { alpha = 1f }
                binding.overlay.alpha = alpha
            }
        })
    }

    private fun render(state: PlaylistInfoState) {
        binding.playlistName.text = state.name
        binding.playlistDescription.text = state.description
        val minutesString = resources.getQuantityString(R.plurals.minutesCount, state.howManyMinutes, state.howManyMinutes)
        val tracksString = resources.getQuantityString(R.plurals.tracksCount, state.howManyTracks, state.howManyTracks)
        binding.howManyMinutes.text = minutesString
        binding.howManyTracks.text = tracksString
        binding.titleInBottomSheet.text = state.name
        binding.numTracksInBottomSheet.text = minutesString
        if (state.imageFileName.isEmpty()) {
            binding.coverImage.setImageResource(R.drawable.placeholder_big)
            binding.pictureInBottomSheet.setImageResource(R.drawable.placeholder_big)
        } else {
            val params = binding.coverImage.layoutParams as ConstraintLayout.LayoutParams
            params.setMargins(0, 0, 0, 0)
            binding.coverImage.layoutParams = params
            binding.coverImage.setImageURI(state.imageFileName.toUri())
            binding.pictureInBottomSheet.setImageURI(state.imageFileName.toUri())
        }
        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(state.tracks)
        trackAdapter.notifyDataSetChanged()
        howManyTracks = state.howManyTracks
        playlistName = state.name
    }

    fun launchPlayerScreen(t: Track) {
        findNavController().navigate(
            R.id.action_playlistInfoFragment_to_playerFragment,
            PlayerFragment.createArgs(t)
        )
    }

    private fun deleteTrackDialog(track: Track): Boolean {
        binding.overlay.visibility = View.VISIBLE
        MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialogTheme2)
            .setTitle("Хотите удалить трек?")
            .setNegativeButton("НЕТ") { dialog, which ->
                binding.overlay.visibility = View.GONE
            }
            .setPositiveButton("ДА") { dialog, which ->
                viewModel.deleteTrackFromPlaylist(track)
                binding.overlay.visibility = View.GONE
            }
            .show()
        return true
    }

    private fun sharePlaylist() {
        if (howManyTracks > 0) {
            binding.overlay2.visibility = View.VISIBLE
            viewModel.sharePlaylist()
            binding.overlay2.visibility = View.GONE
        } else {
            Toast.makeText(requireContext(),
                getString(R.string.no_tracks_for_sharing), Toast.LENGTH_SHORT).show()
        }
    }

    private fun deletePlaylist() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.overlay2.visibility = View.VISIBLE
        MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialogTheme2)
            .setTitle("Хотите удалить плейлист \"$playlistName\"?")
            .setNegativeButton("НЕТ") { dialog, which ->
                binding.overlay2.visibility = View.GONE
            }
            .setPositiveButton("ДА") { dialog, which ->
                viewModel.deletePlaylist()
                findNavController().navigateUp()
            }
            .show()
    }

    private fun editPlaylist() {
        findNavController().navigate(
            R.id.action_playlistInfoFragment_to_createPlaylistFragment,
            CreatePlaylistFragment.createArgs(playlistId ?: CreatePlaylistFragment.NEW_PLAYLIST)
        )
    }


}