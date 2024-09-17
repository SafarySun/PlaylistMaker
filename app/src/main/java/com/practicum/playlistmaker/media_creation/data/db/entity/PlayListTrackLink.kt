package com.practicum.playlistmaker.media_creation.data.db.entity

import androidx.room.Entity

@Entity(tableName = "playlist_track_link",primaryKeys = ["playlistId", "trackId"])

data class PlayListTrackLink(
    val trackId: Int,
    val playlistId: Int,
    val time: Long
)
