package com.practicum.playlistmaker.audioplayer.domain.api

interface PlayerListern {
    fun onPrepared()
    fun onCompletion()
}