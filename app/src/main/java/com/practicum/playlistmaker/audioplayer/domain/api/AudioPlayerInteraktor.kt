package com.practicum.playlistmaker.audioplayer.domain.api

import com.practicum.playlistmaker.audioplayer.domain.OnpreparedOnCompletion
import com.practicum.playlistmaker.audioplayer.domain.impl.PlayerState

interface AudioPlayerInteraktor {
    fun playbackControl()
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(previewUrl:String,listner : OnpreparedOnCompletion)
    fun release()
    fun provideCurrentPosition(): Int
    fun provideState(): PlayerState
    fun isPlaying():Boolean
}
