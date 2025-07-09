package com.example.playlistmaker.ui.library.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityLibraryBinding

class LibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLibraryBinding.inflate(layoutInflater)
        //enableEdgeToEdge()
        setContentView(binding.root)
        //ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.library)) { v, insets ->
        //    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        //    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        //    insets
        //}
        binding.toolbar.setNavigationOnClickListener { finish() }
    }
}