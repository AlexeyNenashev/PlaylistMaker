package com.example.playlistmaker.ui.library.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSelectedTracksBinding
import com.example.playlistmaker.ui.library.SelectedTracksState
import com.example.playlistmaker.ui.library.view_model.SelectedTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectedTracksFragment : Fragment() {
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

        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is SelectedTracksState.NoTracks -> showNoTracks()
                is SelectedTracksState.Content -> { }
            }
        }
    }

    private fun showNoTracks() {
        binding.messageLayout.visibility = View.VISIBLE
    }


    companion object {
        fun newInstance() = SelectedTracksFragment()
    }
}