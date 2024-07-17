package com.practicum.playlistmaker.audioplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.audioplayer.view_model.PlayerState
import com.practicum.playlistmaker.audioplayer.view_model.PlayerViewModel
import com.practicum.playlistmaker.audioplayer.view_model.TrackScreenState
import com.practicum.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.formatDuration
import com.practicum.playlistmaker.utils.formatYear
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class AudioPlayerFragment : Fragment() {
    companion object {
        private const val RADIUS = 16
    }
    private lateinit var binding: FragmentAudioPlayerBinding

    private lateinit var track: Track

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

        // track = intent.getParcelableExtra(SearchFragment.TRANSITION)!!

        val args: AudioPlayerFragmentArgs by navArgs()
        track = args.trackId

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


    private fun setupUi() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        with(binding) {
            itemCountry.text = track.country
            itemGenre.text = track.primaryGenreName
            itemYear.text = track.releaseDate.formatYear()
            titleArtist.text = track.artistName
            itemDuration.text = formatDuration(track.trackTimeMillis)
            titleAlbum.text = track.trackName
            binding.time.text = getString(R.string.time_zero)

            btnPlayPause.setOnClickListener {
                playbackControl()

            }
            itemAlbum.isVisible = track.collectionName.isNotBlank()
            itemAlbum.text = track.collectionName
        }
    }

    private fun playbackControl() {
        viewModel.playbackControler()
        //   if (viewModel.isPlaying()) {
        //      binding.btnPlayPause.setImageResource(R.drawable.play)
        //   } else {
        //      binding.btnPlayPause.setImageResource(R.drawable.pause)
        //   }
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


    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

}
