package com.practicum.playlistmaker.audioplayer.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayer
import com.practicum.playlistmaker.audioplayer.domain.api.PlayerListern

class AudioPlayerImpl : AudioPlayer {
    private val mediaPlayer = MediaPlayer()


    override fun startPlayer() {
        mediaPlayer.start()

    }

    override fun pausePlayer() {
        mediaPlayer.pause()

    }

    override fun preparePlayer(previewUrl: String, listner: PlayerListern) {
        mediaPlayer.apply {
            reset()
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener {
                listner.onPrepared()
            }
            setOnCompletionListener {
                seekTo(0)
                listner.onCompletion()
            }

        }
    }

    override fun reset() {
        mediaPlayer.reset()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun provideCurrentPosition(): Int {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.currentPosition
        } else {
            0
        }

        return mediaPlayer.currentPosition
    }

    override fun isPlaying(): Boolean = mediaPlayer.isPlaying

}