package com.practicum.playlistmaker.audioplayer.domain.api

import com.practicum.playlistmaker.audioplayer.domain.PlayerListern

interface AudioPlayer {
    fun playbackControl()
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(previewUrl:String,listner:PlayerListern)
    fun release()
    fun provideCurrentPosition(): Int

    fun isPlaying():Boolean
    }