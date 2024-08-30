package com.practicum.playlistmaker.media_creation.data.db.repository

import com.practicum.playlistmaker.converters.PlayListDbConverts
import com.practicum.playlistmaker.converters.TrackDbConvert
import com.practicum.playlistmaker.media_creation.data.db.entity.PlayListEntity
import com.practicum.playlistmaker.media_creation.domain.api.PlayListCreationRepository
import com.practicum.playlistmaker.media_creation.domain.model.PlayList
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlayListCreationRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playListDbConverter: PlayListDbConverts,
    private val trackDbConverter: TrackDbConvert
) : PlayListCreationRepository {

    override suspend fun insertPlayList(playlist: PlayList) {
        appDatabase.playlistDao().insertPlayList(playListDbConverter.map(playlist))
    }


     override suspend fun deletePlaylist(playlist: PlayList){
         appDatabase.playlistDao().deletePlaylist(playListDbConverter.map(playlist))
     }


    override suspend fun addTrackToPlaylist(track: Track, playlist: PlayList) {
        appDatabase.trackInPlaylistDao().addTrackToPlaylist(trackDbConverter.mapTrackInPl(track))
        appDatabase.playlistDao().updatePlayList(playlist.playlistId, playlist.amountTracks)
    }

        override fun getPlayLists(): Flow<List<PlayList>> =
            appDatabase.playlistDao().getPlayLists().map { convertFromPlayListEntity(it) }

        private fun convertFromPlayListEntity(playList: List<PlayListEntity>): List<PlayList> {
            return playList.map { newPlayList -> playListDbConverter.map(newPlayList) }
        }


    }