package com.practicum.playlistmaker.audioplayer.presentation

sealed class PlayerState {
    object Default : PlayerState()
    data class Play( val progress : Long) : PlayerState()
    object Pause : PlayerState()
    object Prepared : PlayerState()
}
