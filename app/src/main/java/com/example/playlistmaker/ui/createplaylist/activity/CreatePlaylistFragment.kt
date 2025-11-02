package com.example.playlistmaker.ui.createplaylist.activity

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import com.example.playlistmaker.ui.createplaylist.view_model.CreatePlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CreatePlaylistFragment : Fragment() {

    companion object {
        private const val ARGS_PLAYLIST_ID = "playlist_id"
        fun createArgs(playlistId: Int): Bundle = bundleOf(ARGS_PLAYLIST_ID to playlistId)
        const val NEW_PLAYLIST = -1
    }

    private var playlistId: Int? = null
    private val viewModel: CreatePlaylistViewModel by viewModel<CreatePlaylistViewModel> { parametersOf(playlistId) }
    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        playlistId = requireArguments().getInt(ARGS_PLAYLIST_ID)
        if (playlistId == NEW_PLAYLIST) { playlistId = null }
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (playlistId == null) {
            binding.toolbar.setTitle("Новый плейлист")
            binding.createButton.text = "Создать"
        } else {
            binding.toolbar.setTitle("Редактировать")
            binding.createButton.text = "Сохранить"
        }

        binding.toolbar.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.createButton.isEnabled = false
        binding.createButton.setOnClickListener {
            viewModel.createOrUpdatePlaylist(
                binding.nameInput.text.toString(),
                binding.descriptionInput.text.toString(),
                imageUri
            )
            if (playlistId == null) {
                Toast.makeText(
                    requireContext(),
                    "Плейлист ${binding.nameInput.text.toString()} создан",
                    Toast.LENGTH_SHORT
                ).show()
            }
            findNavController().navigateUp()
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri -> setImage(uri) }

        binding.addPhotoButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.nameInput.doOnTextChanged { text, start, count, after ->
            if (text.isNullOrEmpty()) {
                binding.createButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                binding.createButton.isEnabled = false
            } else {
                binding.createButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
                binding.createButton.isEnabled = true
            }
        }

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            binding.nameInput.setText(state.name)
            binding.descriptionInput.setText(state.description)
            if (state.pathToImage.isNotEmpty()) { setImage(state.pathToImage.toUri()) }
        }

        if (playlistId == null) {
            requireActivity()
                .onBackPressedDispatcher
                .addCallback(
                    this,
                    object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            if (imageUri == null && binding.nameInput.text.isNullOrEmpty() && binding.descriptionInput.text.isNullOrEmpty()) {
                                findNavController().navigateUp()
                            } else {
                                MaterialAlertDialogBuilder(
                                    requireContext(),
                                    R.style.MyAlertDialogTheme
                                )
                                    .setTitle("Завершить создание плейлиста?")
                                    .setMessage("Все несохраненные данные будут потеряны")
                                    .setNegativeButton("Отмена") { dialog, which ->
                                    }
                                    .setPositiveButton("Завершить") { dialog, which ->
                                        findNavController().navigateUp()
                                    }
                                    .show()
                            }

                        }
                    }
                )
        }

    }

    private fun setImage(uri: Uri?) {
        if (uri != null) {
            binding.addPhotoButton.setImageURI(uri)
            binding.addPhotoButton.scaleType = ImageView.ScaleType.CENTER_CROP
            imageUri = uri
        }
    }

}