package com.practicum.playlistmaker.audioplayer.presentation.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.audioplayer.domain.OnpreparedOnCompletion
import com.practicum.playlistmaker.databinding.AudioPlayerBinding
import com.practicum.playlistmaker.formatDuration
import com.practicum.playlistmaker.formatYear
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.presentation.ui.SearchActivity


class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: AudioPlayerBinding
    private val interactorImpl = Creator.getInteractorPlayer()
    private var mainThreadHandler = Handler(Looper.getMainLooper())
    private lateinit var track: Track
    private lateinit var timerRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        @Suppress("DEPRECATION")
        track = intent.getParcelableExtra(SearchActivity.TRANSITION)!!
        setupUi()
        setupImage(track)
        setupTimer()

        interactorImpl.preparePlayer(track.previewUrl, listner = object : OnpreparedOnCompletion {
            override fun onPrepared() {
                binding.btnPlayPause.isEnabled = true
            }

            override fun onCompletion() {
                binding.time.text = getString(R.string.time_zero)
                binding.btnPlayPause.setImageResource(R.drawable.play)
            }
        })
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
            time.text = formatDuration(interactorImpl.provideCurrentPosition().toLong())
            btnPlayPause.setOnClickListener {
                playbackControl()
            }
            itemAlbum.isVisible = track.collectionName.isNotBlank()
            itemAlbum.text = track.collectionName
        }
    }

    private fun playbackControl() {
        if (interactorImpl.isPlaying()) {
            interactorImpl.pausePlayer()
            mainThreadHandler.removeCallbacks(timerRunnable)
            binding.btnPlayPause.setImageResource(R.drawable.play)
        } else {
            interactorImpl.startPlayer()
            binding.btnPlayPause.setImageResource(R.drawable.pause)
            mainThreadHandler.post(timerRunnable)
        }
    }

    private fun setupTimer() {
        timerRunnable = Runnable {
            if (interactorImpl.isPlaying()) {
                binding.time.text = formatDuration(interactorImpl.provideCurrentPosition().toLong())
                mainThreadHandler.postDelayed(timerRunnable, 300)
            }
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

    override fun onPause() {
        super.onPause()
        interactorImpl.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        interactorImpl.release()

    }

    companion object {
        private const val RADIUS = 16
    }
}


