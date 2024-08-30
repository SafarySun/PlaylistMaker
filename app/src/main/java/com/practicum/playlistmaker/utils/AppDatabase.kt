package com.practicum.playlistmaker.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.media_creation.data.db.dao.PlayListDao
import com.practicum.playlistmaker.media_creation.data.db.dao.TrackInPlaylistDao
import com.practicum.playlistmaker.media_creation.data.db.entity.PlayListEntity
import com.practicum.playlistmaker.media_creation.data.db.entity.TrackInPlaylist
import com.practicum.playlistmaker.media_favorite.data.db.dao.TrackDao
import com.practicum.playlistmaker.media_favorite.data.db.entity.TrackEntity

@Database(
    version = 2,
    entities = [
        TrackEntity::class,
        PlayListEntity::class,
        TrackInPlaylist::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlayListDao

    abstract fun trackInPlaylistDao(): TrackInPlaylistDao
}