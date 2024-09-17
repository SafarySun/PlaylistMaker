package com.practicum.playlistmaker.media_creation.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.media_creation.data.db.entity.PlayListTrackLink

@Dao
interface PlayListTrackLinkDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlayList(trackInPlaylist: PlayListTrackLink)

    @Query("SELECT * FROM playlist_track_link WHERE playlistId = :playlistId")
    suspend fun getTracksByPlayListId(playlistId: Int): List<PlayListTrackLink>

    @Query("DELETE FROM playlist_track_link WHERE trackId = :trackId AND playlistId = :playlistId")
    suspend fun deleteTrack(trackId: Int, playlistId: Int)

    @Query("SELECT * FROM playlist_track_link WHERE trackId = :trackId")
    suspend fun getAllByTrackId(trackId: Int): List<PlayListTrackLink>

    @Query("DELETE FROM playlist_track_link WHERE playlistId = :playlistId")
    suspend fun deleteTracksInPlaylist(playlistId: Int)
}