package com.practicum.playlistmaker.media_favorite.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.media_favorite.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)
    @Delete
    suspend fun deleteTrack(trackEntity: TrackEntity)

    @Query("SELECT * FROM track_table ORDER BY timestamp DESC")
    fun getTrack(): Flow<List<TrackEntity>>

    @Query("SELECT id_track FROM track_table")
    suspend fun getId(): List<Int>
}