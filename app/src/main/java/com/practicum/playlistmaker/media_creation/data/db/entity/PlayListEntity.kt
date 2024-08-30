package com.practicum.playlistmaker.media_creation.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "playlist_table")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int,
    val name:String,
    val discription:String,
    val coverImage : String,
    val tracksId: String,
    val amountTracks: Int
)
