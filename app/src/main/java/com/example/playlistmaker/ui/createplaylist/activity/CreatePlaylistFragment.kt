package com.example.playlistmaker.ui.createplaylist.activity

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import androidx.activity.OnBackPressedCallback
import com.example.playlistmaker.ui.createplaylist.view_model.CreatePlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : Fragment() {

    private val viewModel: CreatePlaylistViewModel by viewModel()
    private lateinit var binding: FragmentCreatePlaylistBinding
    private var textWatcher: TextWatcher? = null
    private var imageUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.createButton.isEnabled = false
        binding.createButton.setOnClickListener {
            viewModel.createPlaylist(
                binding.nameInput.text.toString(),
                binding.descriptionInput.text.toString(),
                imageUri
            )
            Toast.makeText(requireContext(), "Плейлист ${binding.nameInput.text.toString()} создан", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    binding.addPhotoButton.setImageURI(uri)
                    imageUri = uri
                    //saveImageToPrivateStorage(uri)
                } //else {
                    //Log.d("PhotoPicker", "No media selected")
                //}
            }

        binding.addPhotoButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable?) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.createButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                    binding.createButton.isEnabled = false
                } else {
                    binding.createButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
                    binding.createButton.isEnabled = true
                }
            }
        }
        textWatcher?.let { binding.nameInput.addTextChangedListener(it) }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        isEnabled = (imageUri == null) && binding.nameInput.text.isNullOrEmpty() && binding.descriptionInput.text.isNullOrEmpty()

                        //Log.d(TAG, "Fragment back pressed invoked")
                        // Do custom work here

                        // if you want onBackPressed() to be called as normal afterwards
                        if (isEnabled) {
                            //isEnabled = false
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        } else {
                            MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialogTheme)
                                .setTitle("Завершить создание плейлиста?") // Заголовок диалога
                                .setMessage("Все несохраненные данные будут потеряны") // Описание диалога
                                .setNegativeButton("Отмена") { dialog, which -> // Добавляет кнопку «Нет»
                                    // Действия, выполняемые при нажатии на кнопку «Нет»
                                }
                                .setPositiveButton("Завершить") { dialog, which -> // Добавляет кнопку «Да»
                                    // Действия, выполняемые при нажатии на кнопку «Да»
                                    findNavController().navigateUp()
                                }
                                .show()
                        }

                    }
                }
            )

    }

}