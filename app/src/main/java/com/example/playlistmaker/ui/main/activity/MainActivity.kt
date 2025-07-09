package com.example.playlistmaker.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.library.activity.LibraryActivity
import com.example.playlistmaker.ui.search.activity.SearchActivity
import com.example.playlistmaker.ui.settings.activity.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button1 = findViewById<Button>(R.id.button_search)
        button1.setOnClickListener {
            //Toast.makeText(this@MainActivity, "Нажали кнопку Поиск", Toast.LENGTH_SHORT).show()
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }


        val button2 = findViewById<Button>(R.id.button_library)
        button2.setOnClickListener {
            val displayIntent = Intent(this, LibraryActivity::class.java)
            startActivity(displayIntent)
        }

        val button3 = findViewById<Button>(R.id.button_settings)
        button3.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }

    }
}