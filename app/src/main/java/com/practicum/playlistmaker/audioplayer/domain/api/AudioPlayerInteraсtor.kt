package com.practicum.playlistmaker.audioplayer.domain.api

interface AudioPlayerInteraсtor {
    fun reset()
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(previewUrl: String, listner: PlayerListener)
    fun release()
    fun provideCurrentPosition():Long

    fun isPlaying(): Boolean
}
