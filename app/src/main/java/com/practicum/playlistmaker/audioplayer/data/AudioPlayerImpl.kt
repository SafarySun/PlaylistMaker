package com.practicum.playlistmaker.audioplayer.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.audioplayer.domain.PlayerListern
import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayer

class AudioPlayerImpl : AudioPlayer {
    private  val mediaPlayer = MediaPlayer()


    override fun playbackControl() {
        if (mediaPlayer.isPlaying)  pausePlayer() else startPlayer()

    }

    override fun startPlayer() {
        mediaPlayer.start()

    }

    override fun pausePlayer() {
        mediaPlayer.pause()

    }

    override fun preparePlayer(previewUrl: String, listner: PlayerListern) {
        mediaPlayer.apply {
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener {
                listner.onPrepared()
            }
            setOnCompletionListener {
                listner.onCompletion()
            }

        }
    }

    override fun release() {
        mediaPlayer?.release()
    }

    override fun provideCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun isPlaying(): Boolean = mediaPlayer.isPlaying

}