package com.practicum.playlistmaker.media_favorite.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

   suspend fun insertTracks(track: Track)

   suspend fun deleteTrack(track: Track)


    fun getTrack(): Flow<List<Track>>
}