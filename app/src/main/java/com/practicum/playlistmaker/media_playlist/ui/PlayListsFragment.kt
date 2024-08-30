package com.practicum.playlistmaker.media_playlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.media.MediaLibraryFragmentDirections
import com.practicum.playlistmaker.media_creation.domain.model.PlayList
import com.practicum.playlistmaker.media_playlist.ui.recycler.PlayListAdapter
import com.practicum.playlistmaker.media_playlist.view_model.PlayListContentState
import com.practicum.playlistmaker.media_playlist.view_model.PlayListFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListsFragment: Fragment() {

    private val viewModel:PlayListFragmentViewModel by viewModel()


    private lateinit var binding: FragmentPlaylistsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.getPlayListContentState().observe(viewLifecycleOwner) {
            render(it)
        }
        binding.newPlaylistButton.setOnClickListener{
            findNavController().navigate(MediaLibraryFragmentDirections.actionMediaLibraryFragmentToPlaylistCreationFragment())
        }


    }
    private fun render(state:PlayListContentState){
        when(state){
            PlayListContentState.Empty -> showError()
           is PlayListContentState.Content -> showContent(state.playlists)
        }

    }
    fun showContent(playlist:List<PlayList>) {
        binding.icon.isVisible = false
        binding.textAboutEmpty.isVisible =false
        binding.recyclerView.isVisible = true
        binding.recyclerView.adapter = PlayListAdapter(playlist)
    }
    private  fun showError() {
        binding.icon.isVisible = true
        binding.textAboutEmpty.isVisible = true
        binding.recyclerView.isVisible = false
    }
    companion object {
        fun newInstance() = PlayListsFragment()
    }
    }
