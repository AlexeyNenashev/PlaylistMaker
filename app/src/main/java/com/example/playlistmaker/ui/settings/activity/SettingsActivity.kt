package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.App
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private var viewModel: SettingsViewModel? = null
    private lateinit var binding: ActivitySettingsBinding








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
        //    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        //    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        //    insets
        //}

        viewModel = ViewModelProvider(this, SettingsViewModel.Companion.getFactory())
            .get(SettingsViewModel::class.java)

        viewModel?.observeDarkTheme()?.observe(this) {
            (applicationContext as App).switchTheme(it)
            binding.nightTheme.isChecked = it
        }

        //val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        binding.toolbar.setNavigationOnClickListener { finish() }



        //val themeSwitcher = findViewById<SwitchMaterial>(R.id.night_theme)
        //binding.nightTheme.isChecked = (applicationContext as App).darkTheme  // тоже через LiveData?
        binding.nightTheme.setOnCheckedChangeListener { switcher, checked ->
            //(applicationContext as App).switchTheme(checked)  // сделать через LiveData
            viewModel?.rememberDarkTheme(checked)
        }

        //val shareIcon = findViewById<com.google.android.material.textview.MaterialTextView>(R.id.share)
        binding.share.setOnClickListener { viewModel?.shareApp(this) }

        //val supportIcon = findViewById<com.google.android.material.textview.MaterialTextView>(R.id.support)
        binding.support.setOnClickListener { viewModel?.openSupport(this) }

        //val userIcon = findViewById<com.google.android.material.textview.MaterialTextView>(R.id.user)
        binding.user.setOnClickListener { viewModel?.openTerms(this) }

    }
}