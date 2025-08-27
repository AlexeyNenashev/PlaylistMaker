package com.example.playlistmaker.ui.main.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMainBinding
import com.example.playlistmaker.ui.library.activity.LibraryFragment
import com.example.playlistmaker.ui.search.activity.SearchFragment
import com.example.playlistmaker.ui.settings.activity.SettingsFragment

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.buttonSearch.setOnClickListener {
            //findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
        }

        binding.buttonLibrary.setOnClickListener {
            //findNavController().navigate(R.id.action_mainFragment_to_libraryFragment)
        }

        binding.buttonSettings.setOnClickListener {
            //findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
        }

    }


}