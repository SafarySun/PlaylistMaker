package com.practicum.playlistmaker.media_favorite.data

import com.practicum.playlistmaker.media_favorite.data.db.AppDatabase
import com.practicum.playlistmaker.media_favorite.data.db.converters.TrackDbConvert
import com.practicum.playlistmaker.media_favorite.data.db.entity.TrackEntity
import com.practicum.playlistmaker.media_favorite.domain.api.FavoriteRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvert: TrackDbConvert
) : FavoriteRepository {
    override suspend fun insertTracks(tracks: Track) {
        appDatabase.trackDao().insertTrack(trackDbConvert.map(tracks))
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase.trackDao().deleteTrack(trackDbConvert.map(track))
    }

    override fun getTrack(): Flow<List<Track>> = flow{
        val track = appDatabase.trackDao().getTrack()
        emit(convertFromTrackEntity(track))
    }
    private fun convertFromTrackEntity(trackEntity:List<TrackEntity>) : List<Track> {
       return trackEntity.map{track -> trackDbConvert.map(track)}
    }
}