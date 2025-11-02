package com.example.playlistmaker.domain.library.impl

import android.net.Uri
import com.example.playlistmaker.domain.library.ImageFileRepository
import com.example.playlistmaker.domain.library.SavePictureUseCase

class SavePictureUseCaseImpl(private val imageFileRepository: ImageFileRepository) : SavePictureUseCase {
    override fun saveOrUpdatePicture(imageUri: Uri?, oldPath: String): String {
        return imageFileRepository.saveOrUpdatePicture(imageUri, oldPath)
    }
}