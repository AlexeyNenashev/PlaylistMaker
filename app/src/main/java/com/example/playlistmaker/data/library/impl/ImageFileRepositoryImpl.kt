package com.example.playlistmaker.data.library.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.domain.library.ImageFileRepository
import java.io.File
import java.io.FileOutputStream

class ImageFileRepositoryImpl(private val context: Context) : ImageFileRepository {

    override fun saveOrUpdatePicture(imageUri: Uri?, oldPath: String): String {

        if (imageUri == null) {
            return ""
        }

        if (imageUri.toString() == oldPath) {
            return oldPath
        }

        val file = if (oldPath.isEmpty()) {
            val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_album")
            if (!filePath.exists()) { filePath.mkdirs() }
            val timestamp = System.currentTimeMillis()
            val filename = "playlist_cover_$timestamp.jpg"
            File(filePath, filename)
        } else {
            File(oldPath)
        }

        val inputStream = context.contentResolver.openInputStream(imageUri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.toString()
    }

}