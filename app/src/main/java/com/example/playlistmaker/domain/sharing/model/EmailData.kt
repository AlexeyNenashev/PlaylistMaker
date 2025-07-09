package com.example.playlistmaker.domain.sharing.model

data class EmailData(
    val listOfEmails: List<String>,
    val subject: String,
    val text: String,
)