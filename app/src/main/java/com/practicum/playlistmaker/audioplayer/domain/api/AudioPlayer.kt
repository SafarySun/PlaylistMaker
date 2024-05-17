package com.practicum.playlistmaker.audioplayer.domain.api

interface AudioPlayer {

    fun startPlayer()
    fun reset()
    fun pausePlayer()
    fun preparePlayer(previewUrl:String,listner: PlayerListener)
    fun release()
    fun provideCurrentPosition(): Long

    fun isPlaying():Boolean
    }