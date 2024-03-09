package com.practicum.playlistmaker.audioplayer.domain.impl

import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayer
import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayerInteraktor

class AudioPlayerInteractorImpl(private val player:AudioPlayer):AudioPlayerInteraktor {
    override fun playbackControl() {
        player.playbackControl()
    }

    override fun startPlayer() {
        player.startPlayer()
    }

    override fun pausePlayer() {
        player.pausePlayer()
    }

    override fun preparePlayer(previewUrl: String) {
        player.preparePlayer(previewUrl)
    }
}