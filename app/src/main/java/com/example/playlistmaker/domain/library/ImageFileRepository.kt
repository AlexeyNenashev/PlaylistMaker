package com.example.playlistmaker.domain.library

import android.net.Uri

interface ImageFileRepository {
    fun savePicture(imageUri: Uri?): String
}