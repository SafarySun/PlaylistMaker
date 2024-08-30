package com.practicum.playlistmaker.media_creation.domain.model

data class PlayList(
    val playlistId :Int = 0,
    val name: String = "",
    val description: String = "",
    val coverImage: String = "",
    val tracksId: ArrayList<Int> = arrayListOf(),
    val amountTracks: Int = 0
)


