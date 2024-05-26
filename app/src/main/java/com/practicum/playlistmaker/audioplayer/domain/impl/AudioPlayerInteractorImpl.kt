package com.practicum.playlistmaker.audioplayer.domain.impl

import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayer
import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayerInteraсtor

class AudioPlayerInteractorImpl(private val player:AudioPlayer):AudioPlayerInteraсtor {


    override fun startPlayer() {
        player.startPlayer()
    }

    override fun pausePlayer() {
        player.pausePlayer()
    }

    override fun preparePlayer(previewUrl: String,listener: AudioPlayerInteraсtor.PlayerListener) {
        player.preparePlayer(previewUrl,listener)

    }
    override fun reset(){
        player.reset()
    }
    override fun release() {
        player.release()
    }
    override fun provideCurrentPosition(): Long{
       return player.provideCurrentPosition()
    }


    override fun isPlaying(): Boolean = player.isPlaying()




}