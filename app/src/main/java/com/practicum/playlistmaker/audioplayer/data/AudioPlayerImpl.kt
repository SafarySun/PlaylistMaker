package com.practicum.playlistmaker.audioplayer.data

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayer

class AudioPlayerImpl(private val mediaPlayer: MediaPlayer) : AudioPlayer {
    private var playerState = STATE_DEFAULT
    private var mainThreadHandler = Handler(Looper.getMainLooper())
    override fun playbackControl() {
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

     override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

     override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun preparePlayer(previewUrl:String) {
        mediaPlayer.apply {
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = STATE_PREPARED
            }
            setOnCompletionListener {
                playerState = STATE_PREPARED
            }
        }
    }

    private fun runnable(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    mainThreadHandler.postDelayed(this, 300)
                }
            }
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}