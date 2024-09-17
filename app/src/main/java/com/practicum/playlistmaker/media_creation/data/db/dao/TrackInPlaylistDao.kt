package com.practicum.playlistmaker.media_creation.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.media_creation.data.db.entity.TrackInPlaylist
@Dao
interface TrackInPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlaylist(track: TrackInPlaylist)

    @Query("DELETE FROM track_in_playlist_table WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: Int)

    @Query("SELECT * FROM track_in_playlist_table WHERE trackId in (:tracksIds)")
    suspend fun getTracks(tracksIds: List<Int>): List<TrackInPlaylist>
}