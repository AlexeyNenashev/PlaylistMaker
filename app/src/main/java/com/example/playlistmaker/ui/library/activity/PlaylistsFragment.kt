package com.example.playlistmaker.ui.library.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.library.PlaylistsState
import com.example.playlistmaker.ui.library.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistsViewModel by viewModel()
    private val playlistsForAdapter = ArrayList<Playlist>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = PlaylistAdapter(playlistsForAdapter)

        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistsState.NoPlaylists -> showNoPlaylists()
                is PlaylistsState.Content -> showPlaylists(it.playlists)
            }
        }

        binding.messageButton.setOnClickListener { launchNewPlaylistScreen() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.showPlaylists()
    }

    private fun showNoPlaylists() {
        binding.noPlaylistsMessage.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun showPlaylists(playlists: List<Playlist>) {
        binding.noPlaylistsMessage.visibility = View.GONE
        playlistsForAdapter.clear()
        playlistsForAdapter.addAll(playlists)
        binding.recyclerView.adapter?.notifyDataSetChanged()
        binding.recyclerView.visibility = View.VISIBLE
    }

    fun launchNewPlaylistScreen() {
        findNavController().navigate(
            R.id.action_libraryFragment_to_createPlaylistFragment
        )
    }


    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}