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
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SettingsViewModel.Companion.getFactory(this))
            .get(SettingsViewModel::class.java)

        viewModel?.observeDarkTheme()?.observe(this) {
            (applicationContext as App).switchTheme(it)
            binding.nightTheme.isChecked = it
        }

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.nightTheme.setOnCheckedChangeListener { switcher, checked ->
            viewModel?.rememberDarkTheme(checked)
        }

        binding.share.setOnClickListener { viewModel?.shareApp() }

        binding.support.setOnClickListener { viewModel?.openSupport() }

        binding.user.setOnClickListener { viewModel?.openTerms() }

    }
}