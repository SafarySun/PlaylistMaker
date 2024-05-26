package com.practicum.playlistmaker.audioplayer.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayer
import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayerInteraсtor

class AudioPlayerImpl(private val mediaPlayer: MediaPlayer) : AudioPlayer {



    override fun startPlayer() {
        mediaPlayer.start()

    }

    override fun pausePlayer() {
        mediaPlayer.pause()

    }

    override fun preparePlayer(previewUrl: String, listener: AudioPlayerInteraсtor.PlayerListener) {
        mediaPlayer.apply {
            reset()
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener {
                listener.onPrepared()
            }
            setOnCompletionListener {
                seekTo(0)
                listener.onCompletion()
            }

        }
    }

    override fun reset() {
        mediaPlayer.reset()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun provideCurrentPosition(): Long {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.currentPosition
        } else {
            0
        }

        return mediaPlayer.currentPosition.toLong()
    }

    override fun isPlaying(): Boolean = mediaPlayer.isPlaying

}