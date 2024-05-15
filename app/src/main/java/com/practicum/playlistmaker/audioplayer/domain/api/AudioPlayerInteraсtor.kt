package com.practicum.playlistmaker.audioplayer.domain.api

interface AudioPlayerInteraсtor {
    fun reset()
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(previewUrl: String, listner: PlayerListern)
    fun release()
    fun provideCurrentPosition(): Int

    fun isPlaying(): Boolean
}
