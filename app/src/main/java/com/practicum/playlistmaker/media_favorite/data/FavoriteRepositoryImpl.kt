package com.practicum.playlistmaker.media_favorite.data

import com.practicum.playlistmaker.utils.AppDatabase
import com.practicum.playlistmaker.converters.TrackDbConvert
import com.practicum.playlistmaker.media_favorite.data.db.entity.TrackEntity
import com.practicum.playlistmaker.media_favorite.domain.api.FavoriteRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvert: TrackDbConvert
) : FavoriteRepository {
    override suspend fun insertTracks(track: Track) {
        appDatabase.trackDao().insertTrack(trackDbConvert.map(track))
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase.trackDao().deleteTrack(trackDbConvert.map(track))
    }


    override fun getTrack(): Flow<List<Track>> = appDatabase.trackDao().getTrack().map {
        convertFromTrackEntity(it)
    }

    private fun convertFromTrackEntity(trackEntity:List<TrackEntity>) : List<Track> {
       return trackEntity.map{track -> trackDbConvert.map(track)}
    }
}