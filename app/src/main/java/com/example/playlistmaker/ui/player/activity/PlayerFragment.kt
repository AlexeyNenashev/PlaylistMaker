package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.player.AddTrackToPlaylistResult
import com.example.playlistmaker.ui.player.PlayerState
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {

    companion object {

        private const val ARGS_TRACK = "track"

        fun createArgs(track: Track): Bundle =
            bundleOf(ARGS_TRACK to track)

    }

    private var track: Track? = null
    private val viewModel by viewModel<PlayerViewModel> {
        parametersOf(track)
    }
    private lateinit var binding: FragmentPlayerBinding
    private var isTextRendered = false
    private val playlistsForAdapter = ArrayList<Playlist>()
    lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        //Log.d("playlists", "hide bottom sheet")

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

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = PlaylistAdapterInPlayer(playlistsForAdapter) { playlist, position -> addTrackToPlaylist(playlist, position) }

        binding.menuButton.setOnClickListener { findNavController().navigateUp() }
        binding.newPlaylistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            launchNewPlaylistScreen()
        }
        track = requireArguments().getParcelable(ARGS_TRACK)
        if (track != null) {
            viewModel.observePlayerState().observe(viewLifecycleOwner) {
                render(it)
            }
            binding.playButton.setOnClickListener {
                viewModel.onPlayButtonClicked()
            }
            binding.heartButton.setOnClickListener {
                viewModel.onFavoriteClicked()
            }
            binding.plusButton.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        viewModel.observePlaylistsState().observe(viewLifecycleOwner) {
            showPlaylists(it)
        }

        viewModel.observeAddTrack().observe(viewLifecycleOwner) {
            reactToAddTrack(it)
        }

        //Log.d("playlists", "viewModel.showPlaylists - starting")
        viewModel.showPlaylists()
        //Log.d("playlists", "viewModel.showPlaylists - done")
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    private fun render(state: PlayerState) {
        setPlayButtonState(state.isPlaying)
        setHeartButtonState(state.isFavorite)
        binding.timeNow.text = state.progressTime
        if (!isTextRendered) {
            isTextRendered = true
            val radius: Int = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10.0F,
                binding.cover.resources.displayMetrics
            ).toInt()
            Glide.with(this)
                .load(state.artworkUrlCover)
                .placeholder(R.drawable.placeholder_big)
                .fitCenter()
                .transform(RoundedCorners(radius))
                .into(binding.cover)
            binding.title.text = state.trackName
            binding.author.text = state.artistName
            binding.duration.text = state.trackTime
            if (state.collectionName.isEmpty()) {
                binding.album.visibility = View.GONE
                binding.albumHeader.visibility = View.GONE
            } else {
                binding.album.text = state.collectionName
                binding.album.visibility = View.VISIBLE
                binding.albumHeader.visibility = View.VISIBLE
            }
            binding.year.text = state.year
            binding.genre.text = state.primaryGenreName
            binding.country.text = state.country
        }
    }

    private fun setPlayButtonState(isPlaying: Boolean) {
        if (isPlaying) {
            binding.playButton.setImageResource(R.drawable.button_pause)
        } else {
            binding.playButton.setImageResource(R.drawable.button_play)
        }
    }

    private fun setHeartButtonState(isFavorite: Boolean) {
        if (isFavorite) {
            binding.heartButton.setImageResource(R.drawable.button_heart_on)
        } else {
            binding.heartButton.setImageResource(R.drawable.button_heart_off)
        }
    }

    private fun showPlaylists(playlists: List<Playlist>) {
        playlistsForAdapter.clear()
        playlistsForAdapter.addAll(playlists)
        binding.recyclerView.adapter?.notifyDataSetChanged()
        //Log.d("playlists", "${playlists.size} playlists found.")
        //Log.d("playlists", "showPlaylists - done")
    }

    private fun launchNewPlaylistScreen() {
        findNavController().navigate(
            R.id.action_playerFragment_to_createPlaylistFragment
        )
    }

    private fun addTrackToPlaylist(playlist: Playlist, position: Int) {
        if (track != null) {
            viewModel.addTrackToPlaylist(playlist, position)
        }
    }

    private fun reactToAddTrack(result: AddTrackToPlaylistResult) {
        when (result) {
            is AddTrackToPlaylistResult.Success -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                playlistsForAdapter[result.position] = result.playlist
                binding.recyclerView.adapter?.notifyItemChanged(result.position)
                Toast.makeText(requireContext(), "Добавлено в плейлист ${result.playlist.name}", Toast.LENGTH_SHORT).show()
            }
            is AddTrackToPlaylistResult.TrackWasAlreadyAdded -> {
                Toast.makeText(requireContext(), "Трек уже добавлен в плейлист ${result.playlistName}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}