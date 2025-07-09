package com.example.playlistmaker.domain.models

data class EmailData(
    val listOfEmails: List<String>,
    val subject: String,
    val text: String,
)
