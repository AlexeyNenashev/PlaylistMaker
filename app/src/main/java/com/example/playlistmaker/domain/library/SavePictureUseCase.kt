package com.example.playlistmaker.domain.library

import android.net.Uri

interface SavePictureUseCase {
    fun saveOrUpdatePicture(imageUri: Uri?, oldPath: String): String
}