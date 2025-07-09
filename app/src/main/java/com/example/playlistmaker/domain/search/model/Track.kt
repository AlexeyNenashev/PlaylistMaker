package com.example.playlistmaker.domain.search.model

data class Track(
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