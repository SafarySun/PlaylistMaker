package com.practicum.playlistmaker.media_edit_info.ui

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.in_playlist.view_model.states.PLDetailsState
import com.practicum.playlistmaker.media_creation.ui.PlaylistCreationFragment
import com.practicum.playlistmaker.media_edit_info.view_model.PlayListEditInfoViewModel
import com.practicum.playlistmaker.media_edit_info.view_model.state.TitleAndButtonState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayListEditInfoFragment : PlaylistCreationFragment() {

    private var playlistId = 0

    override val viewModel: PlayListEditInfoViewModel by viewModel {
        parametersOf(playlistId)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val args: PlayListEditInfoFragmentArgs by navArgs()
        playlistId = args.playlistId

        super.onViewCreated(view, savedInstanceState)

        viewModel.initScreen()

        viewModel.observeDetailsPL().observe(viewLifecycleOwner) { data ->
            renderPlaylistData(data)
        }

        viewModel.observeTitleAndButtonState().observe(viewLifecycleOwner) { data ->
            overrideScreenStrings(data)
        }

  binding.backButton.setOnClickListener{
      findNavController().navigateUp()
  }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
           findNavController().navigateUp()
       }
    }




    private fun renderPlaylistData(data: PLDetailsState) {
        setPlaylistCover(data.image)
            binding.nameEt.setText(data.name)
        binding.descriptionEt.setText(data.description)

    }

    private fun overrideScreenStrings(data: TitleAndButtonState) {
        with(binding) {
            title.text = data.editToolbar
            createButton.text = data.saveButton
        }
    }

    private fun setPlaylistCover(coverUri: String) {
        Glide.with(binding.imageAlbum)
            .load(coverUri)
            .placeholder(R.drawable.placeholder_big)
            .centerCrop()
            .into(binding.imageAlbum)

    }


}

