package com.practicum.playlistmaker.audioplayer.domain.api

interface AudioPlayer {
    fun playbackControl()
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(previewUrl:String)

}