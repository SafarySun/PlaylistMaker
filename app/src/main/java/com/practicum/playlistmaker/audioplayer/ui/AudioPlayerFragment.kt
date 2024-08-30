package com.practicum.playlistmaker.audioplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.audioplayer.ui.recycler.AudioPlayListAdapter
import com.practicum.playlistmaker.audioplayer.view_model.PlayerState
import com.practicum.playlistmaker.audioplayer.view_model.PlayerViewModel
import com.practicum.playlistmaker.audioplayer.view_model.TrackScreenState
import com.practicum.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.practicum.playlistmaker.media_creation.domain.model.PlayList
import com.practicum.playlistmaker.media_playlist.view_model.PlayListContentState
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.formatDuration
import com.practicum.playlistmaker.utils.formatYear
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class AudioPlayerFragment : Fragment() {

    private lateinit var binding: FragmentAudioPlayerBinding

    private lateinit var track: Track

    private var playlistsAdapter = AudioPlayListAdapter {
        viewModel.clickOnPlaylist(it)
    }

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(track)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPlayPause.isEnabled = false

        val args: AudioPlayerFragmentArgs by navArgs()
        track = args.trackId

        val bottomSheetBehavior = BottomSheetBehavior
            .from(binding.playlistsBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }

                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })



        viewModel.getIsFavorite().observe(viewLifecycleOwner) {
            binding.btnFavourite.setImageResource(
                if (it == true) R.drawable.delete_favorite
                else R.drawable.add_favorites
            )

        }

        viewModel.getScreenState().observe(viewLifecycleOwner) {
            when (it) {
                TrackScreenState.Loading -> changeContentVisibility(false)

                TrackScreenState.Content(track) -> {
                    changeContentVisibility(true)
                    setupUi()
                    setupImage(track)
                }

                else -> Unit
            }
        }

        viewModel.getPlayerState().observe(viewLifecycleOwner) {
            when (it) {
                PlayerState.Default -> {
                    binding.btnPlayPause.isEnabled = false
                    binding.btnPlayPause.setImageResource(R.drawable.play)
                }

                PlayerState.Prepared -> {
                    binding.btnPlayPause.isEnabled = true
                    binding.btnPlayPause.setImageResource(R.drawable.play)
                    binding.time.text = getString(R.string.time_zero)
                }

                is PlayerState.Play -> {
                    binding.btnPlayPause.isEnabled = true
                    binding.btnPlayPause.setImageResource(if (viewModel.isPlaying()) R.drawable.pause else R.drawable.play)
                    binding.time.text = formatDuration(viewModel.provideCurrentPosition())
                }

                PlayerState.Pause -> {
                    binding.btnPlayPause.isEnabled = true
                    binding.btnPlayPause.setImageResource(R.drawable.play)
                    binding.time.text = formatDuration(viewModel.provideCurrentPosition())
                }
            }
        }

        viewModel.getPlayListState().observe(viewLifecycleOwner) {state ->
            when (state) {
                PlayListContentState.Empty -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    binding.recyclerViewAdd.isVisible = false

                }
                is PlayListContentState.Content -> {
                    showPlayLists(state.playlists)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                }
            }
        }

        viewModel.observeShowToast().observe(viewLifecycleOwner) {
            showToast(it)
        }

    }

    private fun changeContentVisibility(hasContent: Boolean) {
        with(binding) {
            if (hasContent) {
                itemCountry.isVisible
                itemGenre.isVisible
                itemYear.isVisible
                titleArtist.isVisible
                itemDuration.isVisible
                titleAlbum.isVisible
                time.isVisible
                btnPlayPause.isVisible
                itemAlbum.isVisible

            } else progressBar.isVisible
        }
    }
    private fun showToast(additionalMessage: String?) {
        Toast.makeText(requireContext(), additionalMessage, Toast.LENGTH_SHORT).show()
    }


    private fun setupUi() {
        with(binding) {
            recyclerViewAdd.adapter = playlistsAdapter
            itemCountry.text = track.country
            itemGenre.text = track.primaryGenreName
            itemYear.text = track.releaseDate.formatYear()
            titleArtist.text = track.artistName
            itemDuration.text = formatDuration(track.trackTimeMillis)
            titleAlbum.text = track.trackName
            time.text = getString(R.string.time_zero)
            itemAlbum.isVisible = track.collectionName.isNotBlank()
            itemAlbum.text = track.collectionName

            btnPlayPause.setOnClickListener {
                playbackControl()
            }
            btnFavourite.setOnClickListener {
                viewModel.onFavoriteClicked()
            }
            backButton.setOnClickListener {
                findNavController().navigateUp()
            }
            newPlaylistButton.setOnClickListener {
                findNavController()
                    .navigate(
                        AudioPlayerFragmentDirections
                            .actionAudioPlayerFragmentToPlaylistCreationFragment()
                    )

            }
            btnAdd.setOnClickListener {
                viewModel.addTrackButton()
            }
        }
    }

    private fun playbackControl() {
        viewModel.playbackController()
    }

    private fun setupImage(track: Track) {
        val coverArtworkUrl = track.getCoverArtwork()
        Glide.with(binding.imageAlbum)
            .load(coverArtworkUrl)
            .centerCrop()
            .transform(RoundedCorners(RADIUS))
            .placeholder(R.drawable.placeholder_ap)
            .into(binding.imageAlbum)
    }

    private fun showPlayLists(playlists: List<PlayList>) {
        binding.recyclerViewAdd.isVisible = true
        playlistsAdapter.playLists.clear()
        playlistsAdapter.playLists.addAll(playlists)
        playlistsAdapter.notifyDataSetChanged()
    }
    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    companion object {
        private const val RADIUS = 16
    }

}
