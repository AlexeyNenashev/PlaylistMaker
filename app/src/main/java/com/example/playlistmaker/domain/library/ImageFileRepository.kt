package com.example.playlistmaker.domain.library

import android.net.Uri

interface ImageFileRepository {
    fun saveOrUpdatePicture(imageUri: Uri?, oldPath: String): String
}