package com.practicum.playlistmaker.media_creation.domain.api

import com.practicum.playlistmaker.media_creation.domain.model.PlayList
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayListCreationInteractor {
    suspend fun insertPlayList(playlist: PlayList)

    suspend fun deletePlaylist(playlist: PlayList)

    fun getPlaylist(): Flow<List<PlayList>>
    suspend fun addTrackToPlaylist(track: Track, playlist: PlayList)


}