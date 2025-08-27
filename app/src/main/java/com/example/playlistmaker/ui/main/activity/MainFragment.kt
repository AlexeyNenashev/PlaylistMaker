package com.example.playlistmaker.ui.main.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentMainBinding
import com.example.playlistmaker.ui.library.activity.LibraryActivity
import com.example.playlistmaker.ui.search.activity.SearchFragment
import com.example.playlistmaker.ui.settings.activity.SettingsActivity

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.buttonSearch.setOnClickListener {
            val displayIntent = Intent(requireContext(), SearchFragment::class.java)
            startActivity(displayIntent)
        }

        binding.buttonLibrary.setOnClickListener {
            val displayIntent = Intent(requireContext(), LibraryActivity::class.java)
            startActivity(displayIntent)
        }

        binding.buttonSettings.setOnClickListener {
            val displayIntent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(displayIntent)
        }

    }


}