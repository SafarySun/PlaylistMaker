package com.practicum.playlistmaker.media_creation.data.db.repository

import com.practicum.playlistmaker.converters.PlayListDbConverts
import com.practicum.playlistmaker.converters.TrackDbConvert
import com.practicum.playlistmaker.media_creation.data.db.entity.PlayListEntity
import com.practicum.playlistmaker.media_creation.domain.api.PlayListCreationRepository
import com.practicum.playlistmaker.media_creation.domain.model.PlayList
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlayListCreationRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playListDbConverter: PlayListDbConverts,
    private val trackDbConverter: TrackDbConvert
) : PlayListCreationRepository {

    override suspend fun insertPlayList(playlist: PlayList) {
        appDatabase.playlistDao().insertPlayList(playListDbConverter.map(playlist))
    }


    override suspend fun deletePlaylist(playlist: PlayList) {
        appDatabase.playlistDao().deletePlaylist(playListDbConverter.map(playlist))
    }


    override suspend fun addTrackToPlaylist(track: Track, playlist: PlayList): Boolean {

        if (playlist.tracksId.contains(track.trackId)) return false
        playlist.apply {

            tracksId.add(track.trackId)

            amountTracks += 1

            appDatabase.playlistDao().updatePlayList( playListDbConverter.map(playlist))

            appDatabase.trackInPlaylistDao()
                .addTrackToPlaylist(trackDbConverter.mapTrackInPl(track))
        }
        return true
    }

    override fun getPlayLists(): Flow<List<PlayList>> =
        appDatabase.playlistDao().getPlayLists().map { convertFromPlayListEntity(it) }

    override suspend fun getPlaylist(playlistId: Int): PlayList = withContext(Dispatchers.IO) {
        playListDbConverter.map(appDatabase.playlistDao().getPlaylist(playlistId))
    }

    private fun convertFromPlayListEntity(playList: List<PlayListEntity>): List<PlayList> {
        return playList.map { newPlayList -> playListDbConverter.map(newPlayList) }
    }


}