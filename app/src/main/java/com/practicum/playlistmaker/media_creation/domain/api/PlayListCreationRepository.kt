package com.practicum.playlistmaker.media_creation.domain.api

import com.practicum.playlistmaker.media_creation.domain.model.PlayList
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface PlayListCreationRepository {
    suspend fun insertPlayList(playlist: PlayList)

    suspend fun deletePlaylist(playlist: PlayList)


    suspend fun addTrackToPlaylist(track: Track, playlist: PlayList):Boolean


     fun getPlayLists(): Flow<List<PlayList>>

    suspend fun getPlaylist(playlistId: Int): PlayList




}
