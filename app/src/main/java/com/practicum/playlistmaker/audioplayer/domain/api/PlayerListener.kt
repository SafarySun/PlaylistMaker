package com.practicum.playlistmaker.audioplayer.domain.api

interface PlayerListener {
    fun onPrepared()
    fun onCompletion()
}