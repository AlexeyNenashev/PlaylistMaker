package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.App
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeDarkTheme().observe(viewLifecycleOwner) {
            (requireContext().applicationContext as App).switchTheme(it)
            binding.nightTheme.isChecked = it
        }

        binding.nightTheme.setOnCheckedChangeListener { switcher, checked ->
            viewModel.rememberDarkTheme(checked)
        }

        binding.share.setOnClickListener { viewModel.shareApp() }

        binding.support.setOnClickListener { viewModel.openSupport() }

        binding.user.setOnClickListener { viewModel.openTerms() }

    }
}