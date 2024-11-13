package com.practicum.playlistmaker.media_favorite.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
    @Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey @ColumnInfo(name = "id_track")
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val timestamp: Long = System.currentTimeMillis()
)