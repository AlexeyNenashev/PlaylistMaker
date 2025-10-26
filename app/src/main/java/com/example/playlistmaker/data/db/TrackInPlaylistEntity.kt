package com.example.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_in_playlist_table")
data class TrackInPlaylistEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTime: String, // Продолжительность трека в секундах в формате mm:ss
    val artworkUrl100: String, // Ссылка на изображение обложки
    val artworkUrlCover: String, // Ссылка на изображение обложки в хорошем разрешении
    val collectionName: String, // Название альбома
    val year: String, // Год релиза трека
    val primaryGenreName: String, // Жанр трека
    val country: String, // Страна исполнителя
    val previewUrl: String, // Ссылка на аудио
)