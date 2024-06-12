package com.practicum.playlistmaker.media

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.media_favorite.ui.FavoritesFragment
import com.practicum.playlistmaker.media_playlist.ui.PlayListsFragment

class MediaLibraryPager (fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavoritesFragment.newInstance()
            else -> PlayListsFragment.newInstance()
        }
    }
}