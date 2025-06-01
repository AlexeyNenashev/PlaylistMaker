package com.example.playlistmaker.data.dto

import java.text.SimpleDateFormat
import java.util.Locale

data class TrackDto(
    val trackId: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: String, // Продолжительность трека в миллисекундах
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String, // Название альбома
    val releaseDate: String, // Год релиза трека
    val primaryGenreName: String, // Жанр трека
    val country: String, // Страна исполнителя
    val previewUrl: String, // Ссылка на аудио
) {

    fun getCoverArtwork(): String {
        if (artworkUrl100.isNullOrEmpty()) {
            return ""
        }
        return artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
    }

    fun trackTime(): String {
        if (trackTimeMillis.isNullOrEmpty()) {
            return "00:00"
        }
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format((trackTimeMillis + "").toLongOrNull() ?: 0L)
    }

    fun getYear(): String {
        if (releaseDate.isNullOrEmpty()) {
            return ""
        }
        return if (releaseDate.length >= 4) releaseDate.substring(0,4) else ""
    }

}
