package com.practicum.playlistmaker.media_playlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.PlaylistsFragmentBinding
import com.practicum.playlistmaker.media_playlist.view_model.PlayListContentState
import com.practicum.playlistmaker.media_playlist.view_model.PlayListFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListsFragment: Fragment() {

    private val viewModel:PlayListFragmentViewModel by viewModel()
    companion object {
        fun newInstance() = PlayListsFragment()
            }

    private lateinit var binding: PlaylistsFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = PlaylistsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.getPlayListContentState().observe(viewLifecycleOwner) {
            when(it){
                PlayListContentState.EMPTY -> showError()
                PlayListContentState.CONTENT -> showError()
            }
        }
    }
    fun showError() {
        binding.icon.isVisible = true
        binding.textAboutError.isVisible = true
    }
    }
