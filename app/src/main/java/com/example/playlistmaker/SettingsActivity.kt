package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
        }

        val shareIcon = findViewById<com.google.android.material.textview.MaterialTextView>(R.id.share)
        shareIcon.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.linkToCourse))
                type = "text/plain"
            }
            startActivity(intent)
        }

        val supportIcon = findViewById<com.google.android.material.textview.MaterialTextView>(R.id.support)
        supportIcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.myEmail)))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.emailSubject))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.emailText))
            startActivity(intent)
        }

        val userIcon = findViewById<com.google.android.material.textview.MaterialTextView>(R.id.user)
        userIcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.practicumOffer)))
            startActivity(intent)
        }

    }
}