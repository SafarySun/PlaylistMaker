package com.practicum.playlistmaker.audioplayer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.audioplayer.domain.PlayerListern
import com.practicum.playlistmaker.audioplayer.presentation.PlayerState
import com.practicum.playlistmaker.audioplayer.presentation.PlayerViewModel
import com.practicum.playlistmaker.databinding.AudioPlayerBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.utils.creator.formatDuration
import com.practicum.playlistmaker.utils.creator.formatYear

@Suppress("DEPRECATION")
class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: AudioPlayerBinding
    private lateinit var track: Track
    private val viewModel by lazy {
        ViewModelProvider(
            this, PlayerViewModel.getViewModelFactory()
        )[PlayerViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = intent.getParcelableExtra(SearchActivity.TRANSITION)!!

        viewModel.getPlayerState().observe(this) {

            if(viewModel.getPlayerState().value == PlayerState.STATE_DEFAULT)

            viewModel.preparePlayer(track.previewUrl, listner = object : PlayerListern { //
                override fun onPrepared() {
                    binding.btnPlayPause.isEnabled = true
                }
                override fun onCompletion() {
                    binding.time.text = getString(R.string.time_zero)
                    binding.btnPlayPause.setImageResource(R.drawable.play)
                }
            })
        }


        setupUi()
        setupImage(track)
       setupTimer()


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
            time.text = formatDuration(viewModel.provideCurrentPosition().toLong())
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

    private fun setupTimer() {
       // viewModel.setupTimer()
        binding.time.text = formatDuration(viewModel.provideCurrentPosition().toLong())

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

override fun onDestroy() {
    super.onDestroy()
    viewModel.release()

}

companion object {
    private const val RADIUS = 16
}
}