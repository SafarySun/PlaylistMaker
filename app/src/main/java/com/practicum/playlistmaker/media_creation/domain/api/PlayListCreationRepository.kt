package com.practicum.playlistmaker.media_creation.domain.api

import com.practicum.playlistmaker.media_creation.domain.model.PlayList
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface PlayListCreationRepository {
    suspend fun insertPlayList(playlist: PlayList)

    suspend fun deletePlayListById(playlistId: Int)


    suspend fun addTrackToPlaylist(track: Track, playlist: PlayList)


     fun getPlayLists(): Flow<List<PlayList>>

    suspend fun getPlaylist(playlistId: Int): PlayList

     suspend fun getTracksFromPlayList(playlist: PlayList): List<Track>

    suspend fun deleteTrack(trackId: Int, playlistId: Int)
}
