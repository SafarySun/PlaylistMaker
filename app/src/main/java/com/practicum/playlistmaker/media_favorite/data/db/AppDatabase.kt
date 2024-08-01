package com.practicum.playlistmaker.media_favorite.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.media_favorite.data.db.dao.TrackDao
import com.practicum.playlistmaker.media_favorite.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao():TrackDao
}