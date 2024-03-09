package com.practicum.playlistmaker.audioplayer.domain.api

interface AudioPlayerInteraktor {
    fun playbackControl()
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(previewUrl:String)

}
