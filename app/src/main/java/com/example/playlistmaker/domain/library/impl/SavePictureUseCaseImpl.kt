package com.example.playlistmaker.domain.library.impl

import android.net.Uri
import com.example.playlistmaker.domain.library.ImageFileRepository
import com.example.playlistmaker.domain.library.SavePictureUseCase

class SavePictureUseCaseImpl(private val imageFileRepository: ImageFileRepository) : SavePictureUseCase {
    override fun savePicture(imageUri: Uri?): String {
        return imageFileRepository.savePicture(imageUri)
    }
}