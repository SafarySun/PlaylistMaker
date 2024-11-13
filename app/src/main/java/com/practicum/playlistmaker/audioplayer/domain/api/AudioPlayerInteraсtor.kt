package com.practicum.playlistmaker.audioplayer.domain.api

interface AudioPlayerInteraсtor {
    fun reset()
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(previewUrl: String, listener: PlayerListener)
    fun release()
    fun provideCurrentPosition():Long

    fun isPlaying(): Boolean
    interface PlayerListener {
        fun onPrepared()
        fun onCompletion()
    }
}
