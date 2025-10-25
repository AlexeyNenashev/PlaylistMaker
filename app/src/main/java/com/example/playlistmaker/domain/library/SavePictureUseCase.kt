package com.example.playlistmaker.domain.library

import android.net.Uri

interface SavePictureUseCase {
    fun savePicture(imageUri: Uri?): String
}