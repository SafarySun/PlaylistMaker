package com.practicum.playlistmaker.media_favorite.domain.impl

import com.practicum.playlistmaker.media_favorite.domain.api.FavoriteInteractor
import com.practicum.playlistmaker.media_favorite.domain.api.FavoriteRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(private val favoriteRepository: FavoriteRepository) :
    FavoriteInteractor {
    override suspend fun insertTracks(track: Track) {
        favoriteRepository.insertTracks(track)
    }

    override suspend fun deleteTrack(track: Track) {
        favoriteRepository.deleteTrack(track)
    }

    override fun getTrack(): Flow<List<Track>> {
        return favoriteRepository.getTrack()
    }
}