package com.example.playlistmaker.ui.main.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
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
            //val displayIntent = Intent(requireContext(), SearchFragment::class.java)
            //startActivity(displayIntent)
            parentFragmentManager.commit {
                replace(
                    // Указали, в каком контейнере работаем
                    R.id.rootFragmentContainerView,
                    // Создали фрагмент
                    SearchFragment(),
                    // Указали тег фрагмента
                    SearchFragment.TAG
                )
                // Добавляем фрагмент в Back Stack
                addToBackStack(SearchFragment.TAG)
            }
        }

        binding.buttonLibrary.setOnClickListener {
            //val displayIntent = Intent(requireContext(), LibraryFragment::class.java)
            //startActivity(displayIntent)
            parentFragmentManager.commit {
                replace(
                    // Указали, в каком контейнере работаем
                    R.id.rootFragmentContainerView,
                    // Создали фрагмент
                    LibraryFragment(),
                    // Указали тег фрагмента
                    LibraryFragment.TAG
                )
                // Добавляем фрагмент в Back Stack
                addToBackStack(LibraryFragment.TAG)
            }
        }

        binding.buttonSettings.setOnClickListener {
            //val displayIntent = Intent(requireContext(), SettingsFragment::class.java)
            //startActivity(displayIntent)
            parentFragmentManager.commit {
                replace(
                    // Указали, в каком контейнере работаем
                    R.id.rootFragmentContainerView,
                    // Создали фрагмент
                    SettingsFragment(),
                    // Указали тег фрагмента
                    SettingsFragment.TAG
                )
                // Добавляем фрагмент в Back Stack
                addToBackStack(SettingsFragment.TAG)
            }
        }

    }


}