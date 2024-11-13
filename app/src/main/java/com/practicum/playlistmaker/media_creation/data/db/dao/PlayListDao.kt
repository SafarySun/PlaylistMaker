package com.practicum.playlistmaker.media_creation.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.media_creation.data.db.entity.PlayListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayList(playlist: PlayListEntity)

    @Query("DELETE FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun deletePlayListById(playlistId: Int)

    @Query("SELECT * FROM playlist_table")
    fun getPlayLists(): Flow<List<PlayListEntity>>

    @Query("UPDATE playlist_table SET amountTracks = :amountTracks WHERE playlistId = :playlistId")
    suspend fun updateAmountTracks(playlistId: Int, amountTracks: Int)

    @Query("SELECT * FROM  playlist_table WHERE playlistId = :playlistId")
    suspend fun getPlayList(playlistId: Int): PlayListEntity

    @Query("SELECT amountTracks FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun getAmountTracks(playlistId: Int): Int
}