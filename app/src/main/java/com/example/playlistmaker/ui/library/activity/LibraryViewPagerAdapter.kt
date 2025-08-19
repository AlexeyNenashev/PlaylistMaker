package com.example.playlistmaker.ui.library.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class LibraryViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle)  {

    companion object {
        const val HOW_MANY_FRAGMENTS = 2
    }

    override fun getItemCount(): Int {
        return HOW_MANY_FRAGMENTS
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> SelectedTracksFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }

}