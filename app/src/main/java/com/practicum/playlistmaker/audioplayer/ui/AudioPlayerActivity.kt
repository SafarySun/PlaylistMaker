package com.practicum.playlistmaker.audioplayer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.audioplayer.domain.api.PlayerListener
import com.practicum.playlistmaker.audioplayer.presentation.PlayerState
import com.practicum.playlistmaker.audioplayer.presentation.PlayerViewModel
import com.practicum.playlistmaker.audioplayer.presentation.TrackScreenState
import com.practicum.playlistmaker.databinding.AudioPlayerBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.utils.creator.formatDuration
import com.practicum.playlistmaker.utils.creator.formatYear


class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: AudioPlayerBinding
    private lateinit var track: Track
    private lateinit var viewModel: PlayerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPlayPause.isEnabled = false

        track = intent.getParcelableExtra(SearchActivity.TRANSITION)!!

        val previewUrl = track.previewUrl

        val listener = object : PlayerListener {
            override fun onPrepared() {
                binding.btnPlayPause.isEnabled = true

            }

            override fun onCompletion() {
                binding.time.text = getString(R.string.time_zero)
                binding.btnPlayPause.setImageResource(R.drawable.play)
            }
        }
        viewModel = ViewModelProvider(
            this, PlayerViewModel.getViewModelFactory(track,previewUrl, listener)
        )[PlayerViewModel::class.java]

        viewModel.getScreenState().observe(this) {
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
        viewModel.getPlayerState().observe(this) {
            when (it) {

                PlayerState.Default -> {
                    binding.btnPlayPause.isEnabled = false
                    binding.btnPlayPause.setImageResource(R.drawable.play)
                }
                PlayerState.Prepared -> {
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
            if(hasContent){
                itemCountry.isVisible
                itemGenre.isVisible
                itemYear.isVisible
                titleArtist.isVisible
                itemDuration.isVisible
                titleAlbum.isVisible
                time.isVisible
                btnPlayPause.isVisible
                itemAlbum.isVisible

            }
            else progressBar.isVisible
        }
    }


    private fun setupUi() {
        binding.backButton.setOnClickListener {
            finish()
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
        if (viewModel.isPlaying()) {
            binding.btnPlayPause.setImageResource(R.drawable.play)
        } else {
            binding.btnPlayPause.setImageResource(R.drawable.pause)

        }
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

    companion object {
        private const val RADIUS = 16
    }
}