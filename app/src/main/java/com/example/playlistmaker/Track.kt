package com.example.playlistmaker

data class Track(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: String, // Продолжительность трека в миллисекундах
    val artworkUrl100: String, // Ссылка на изображение обложки
)
