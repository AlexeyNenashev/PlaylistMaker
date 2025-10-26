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

    override fun savePicture(imageUri: Uri?): String {
        if (imageUri == null) {
            return ""
        }

        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_album")
        if (!filePath.exists()) { filePath.mkdirs() }
        val timestamp = System.currentTimeMillis()
        val filename = "playlist_cover_$timestamp.jpg"
        val file = File(filePath, filename)
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.toString()
    }

}