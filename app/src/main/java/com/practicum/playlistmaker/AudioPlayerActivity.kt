package com.practicum.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.databinding.AudioPlayerBinding



class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var  binding :AudioPlayerBinding
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var mainThreadHandler = Handler(Looper.getMainLooper())
    private var track: Track? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        track = intent.getParcelableExtra(SearchActivity.TRANSITION)
        setupUi()
        setupImage(track)
        preparePlayer()

    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
                mainThreadHandler.removeCallbacks(runnable())
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                mainThreadHandler.post(runnable())
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        binding.btnPlayPause.setImageResource(R.drawable.pause)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        binding.btnPlayPause.setImageResource(R.drawable.play)
    }

    private fun preparePlayer() {
        mediaPlayer.apply {
            setDataSource(track?.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                binding.btnPlayPause.isEnabled = true
                playerState = STATE_PREPARED
            }
            setOnCompletionListener {
                playerState = STATE_PREPARED
                binding.time.text = getString(R.string.time_zero)
                binding.btnPlayPause.setImageResource(R.drawable.play)


            }
        }
    }

    private fun runnable(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    binding.time.text = track?.formatDuration(mediaPlayer.currentPosition.toLong())
                    mainThreadHandler.postDelayed(this, 300)
                }
            }
        }
    }

    private fun setupUi() {
        track?.let { track ->
            with(binding) {
                itemCountry.text = track.country
                itemGenre.text = track.primaryGenreName
                itemYear.text = track.releaseDate.formatYear()
                titleArtist.text = track.artistName
                itemDuration.text = track.formatDuration(track.trackTimeMillis)
                titleAlbum.text = track.trackName
                binding.time.text = track?.formatDuration(mediaPlayer.currentPosition.toLong())
                backButton.setOnClickListener {
                    finish()
                }
                binding.btnPlayPause.setOnClickListener {
                    playbackControl()
                }
            }
            binding.itemAlbum.isVisible = track.collectionName.isNotBlank()
            binding.itemAlbum.text = track.collectionName
        }
    }

    private fun setupImage(track: Track?) {
        val coverArtworkUrl = track?.getCoverArtwork()
        Glide.with(this)
            .load(coverArtworkUrl)
            .centerCrop()
            .transform(RoundedCorners(RADIUS))
            .placeholder(R.drawable.placeholder_ap)
            .into(binding.imageAlbum)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()

    }

    companion object {
        private const val RADIUS = 16
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}
    

