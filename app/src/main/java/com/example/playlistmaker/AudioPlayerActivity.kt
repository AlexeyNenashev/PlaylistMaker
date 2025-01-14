package com.example.playlistmaker

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AudioPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val menuButton = findViewById<ImageButton>(R.id.menu_button)
        menuButton.setOnClickListener {
            finish()
        }

        val title = findViewById<TextView>(R.id.title)

        val trackString = intent.getStringExtra(SearchActivity.EXTRA_TRACK)
        if (trackString != null) {
            val titleText: String = trackString
            title.text = titleText

        }






    }
}