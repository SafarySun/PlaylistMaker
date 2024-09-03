package com.practicum.playlistmaker.media_creation.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.practicum.playlistmaker.media_creation.data.db.entity.TrackInPlaylist
@Dao
interface TrackInPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlaylist(track: TrackInPlaylist)

   // @Query("SELECT * FROM tracks_in_pl_table WHERE trackId in (:tracksIds)")
   // suspend fun getTracks(tracksIds: List<Int>): List<TrackInPlaylist>
}