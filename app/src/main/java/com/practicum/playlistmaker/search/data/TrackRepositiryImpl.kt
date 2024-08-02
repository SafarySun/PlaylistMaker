package com.practicum.playlistmaker.search.data

import android.content.Context
import com.practicum.playlistmaker.R.string.error_empty_search
import com.practicum.playlistmaker.R.string.error_internet
import com.practicum.playlistmaker.media_favorite.data.db.AppDatabase
import com.practicum.playlistmaker.search.data.dto.TrackRequest
import com.practicum.playlistmaker.search.data.dto.TrackResponse
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.sharedprefs.LocalStorage
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val sharedPreferences: LocalStorage,
    private val context: Context,
    private val appDatabase: AppDatabase
) : TrackRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackRequest(expression))
        when (response.resultCode) {

            ERROR_NETWORK -> {
                emit(Resource.Error(context.resources.getString(error_internet)))
            }

            SUCCESS -> {

                val favoriteTrackId = appDatabase.trackDao().getId()

                emit(Resource.Success((response as TrackResponse).results.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl,
                        isFavorite = (favoriteTrackId.contains(it.trackId))
                    )
                }))
            }

            else -> {
                emit(Resource.Error(context.resources.getString(error_empty_search)))
            }
        }
    }

    override fun getHistory(): Flow<List<Track>> = flow {
        val favoriteTrackId = appDatabase.trackDao().getId()
        val getHistory = sharedPreferences.loadTracks()
        emit(getHistory.map {
            it.copy(isFavorite = favoriteTrackId.contains(it.trackId))
        })
    }

    override fun addTrackToHistory(track: Track) {
        sharedPreferences.addTrackToHistory(track)
    }

    override fun clearHistory() {
        sharedPreferences.clearHistory()
    }

    companion object {
        private const val SUCCESS = 200
        private const val ERROR_NETWORK = -1
    }
}

