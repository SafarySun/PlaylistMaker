package com.practicum.playlistmaker.audioplayer.domain

interface PlayerListern {
    fun onPrepared()
    fun onCompletion()
}