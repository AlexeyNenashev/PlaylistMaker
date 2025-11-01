package com.example.playlistmaker.ui.playlistinfo.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.example.playlistmaker.ui.playlistinfo.PlaylistInfoState
import com.example.playlistmaker.ui.playlistinfo.view_model.PlaylistInfoViewModel
import org.koin.core.parameter.parametersOf
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistInfoFragment : Fragment() {

    companion object {

        private const val ARGS_PLAYLIST_ID = "playlist_id"

        fun createArgs(playlistId: Int): Bundle =
            bundleOf(ARGS_PLAYLIST_ID to playlistId)

    }

    private var playlistId: Int? = null
    private val viewModel by viewModel<PlaylistInfoViewModel> { parametersOf(playlistId) }
    private var _binding: FragmentPlaylistInfoBinding? = null
    private val binding get() = _binding!!

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
        binding.arrowBack.setOnClickListener { findNavController().navigateUp() }
        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }
        viewModel.showPlaylistInfo()
    }

    private fun render(state: PlaylistInfoState) {
        binding.playlistName.text = state.name
        binding.playlistDescription.text = state.description
        binding.howManyMinutes.text = numberToString(state.howManyMinutes, "минут", "минута", "минуты")
        binding.howManyTracks.text = numberToString(state.howManyTracks, "треков", "трек", "трека")
        if (state.imageFileName.isEmpty()) {
            binding.coverImage.setImageResource(R.drawable.placeholder_big)
        } else {
            val params = binding.coverImage.layoutParams as ConstraintLayout.LayoutParams
            params.setMargins(0, 0, 0, 0)
            binding.coverImage.layoutParams = params
            binding.coverImage.setImageURI(state.imageFileName.toUri())
        }
    }

    private fun numberToString(number: Int, string0: String, string1: String, string2: String): String {
        val n10  = number % 10
        val n100 = number % 100
        var s = "$number $string0"
        if (n10 == 1 && n100 != 11) { s = "$number $string1" }
        if (n10 == 2 && n100 != 12) { s = "$number $string2" }
        if (n10 == 3 && n100 != 13) { s = "$number $string2" }
        if (n10 == 4 && n100 != 14) { s = "$number $string2" }
        return s
    }

}