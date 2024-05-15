package com.practicum.playlistmaker.audioplayer.domain.api

interface AudioPlayer {

    fun startPlayer()
    fun reset()
    fun pausePlayer()
    fun preparePlayer(previewUrl:String,listner: PlayerListern)
    fun release()
    fun provideCurrentPosition(): Int

    fun isPlaying():Boolean
    }