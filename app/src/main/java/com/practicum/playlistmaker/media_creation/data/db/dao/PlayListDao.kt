package com.practicum.playlistmaker.media_creation.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.media_creation.data.db.entity.PlayListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayListDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertPlayList(playlist: PlayListEntity)
        @Delete
        suspend fun deletePlaylist(playlist: PlayListEntity)

        @Query("SELECT * FROM playlist_table")
         fun getPlayLists(): Flow<List<PlayListEntity>>

        @Query("UPDATE playlist_table SET amountTracks = :amountTracks WHERE playlistId = :playlistId")
        suspend fun updatePlayList(playlistId: Int, amountTracks: Int)

        @Query("SELECT * FROM  playlist_table WHERE playlistId = :playlistId")
        suspend fun getPlaylist(playlistId: Int):PlayListEntity

}