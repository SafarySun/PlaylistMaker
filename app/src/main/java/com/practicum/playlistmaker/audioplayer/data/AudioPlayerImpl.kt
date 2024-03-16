package com.practicum.playlistmaker.audioplayer.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.audioplayer.domain.OnpreparedOnCompletion
import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayer
import com.practicum.playlistmaker.audioplayer.domain.impl.PlayerState

class AudioPlayerImpl : AudioPlayer {
    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.STATE_DEFAULT

    override fun playbackControl() {
        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }
            else -> Unit
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
    }

    override fun preparePlayer(previewUrl: String, listner: OnpreparedOnCompletion) {
        mediaPlayer.apply {
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = PlayerState.STATE_PREPARED
                listner.onPrepared()
            }
            setOnCompletionListener {
                playerState = PlayerState.STATE_PREPARED
                listner.onCompletion()
            }

        }
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun provideCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun provideState(): PlayerState {
        return playerState
    }

    override fun isPlaying(): Boolean = mediaPlayer.isPlaying

}