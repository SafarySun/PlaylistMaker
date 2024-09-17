package com.practicum.playlistmaker.media_creation.data.db.repository

import android.util.Log
import com.practicum.playlistmaker.converters.PlayListDbConverts
import com.practicum.playlistmaker.converters.TrackDbConvert
import com.practicum.playlistmaker.media_creation.data.db.entity.PlayListEntity
import com.practicum.playlistmaker.media_creation.data.db.entity.PlayListTrackLink
import com.practicum.playlistmaker.media_creation.domain.api.PlayListCreationRepository
import com.practicum.playlistmaker.media_creation.domain.model.PlayList
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.Calendar

class PlayListCreationRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playListDbConverter: PlayListDbConverts,
    private val trackDbConverter: TrackDbConvert
) : PlayListCreationRepository {


    //ADD
    override suspend fun insertPlayList(playlist: PlayList) {
        appDatabase.playlistDao().insertPlayList(playListDbConverter.map(playlist))
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: PlayList) {
        appDatabase.trackInPlaylistDao().addTrackToPlaylist(trackDbConverter.mapTrackInPl(track))

        appDatabase.playlistTrackLink().addTrackToPlayList(
            PlayListTrackLink(track.trackId, playlist.playlistId, Calendar.getInstance().time.time)
        )

        appDatabase.playlistDao().updateAmountTracks(
            playlist.playlistId,
            playlist.amountTracks
        )
    }

    //DELETE

    override suspend fun deletePlayListById(playlistId: Int) {
        appDatabase.playlistDao().deletePlayListById(playlistId)
        val tracksInPlaylist =
            appDatabase.playlistTrackLink().getTracksByPlayListId(playlistId)
        appDatabase.playlistTrackLink().deleteTracksInPlaylist(playlistId)
        for (track in tracksInPlaylist) {
            checkAndDelete(track.trackId)
        }
    }

    override suspend fun deleteTrack(trackId: Int, playlistId: Int) {
        appDatabase.playlistTrackLink().deleteTrack(trackId, playlistId)
        appDatabase.playlistDao().updateAmountTracks(
            playlistId,
            appDatabase.playlistDao().getAmountTracks(playlistId) - 1
        )
        checkAndDelete(trackId)
    }


    //GET
    override suspend fun getTracksFromPlayList(playlist: PlayList): List<Track> =
        withContext(Dispatchers.IO) {
            val tracksIds =
                appDatabase.playlistTrackLink().getTracksByPlayListId(playlist.playlistId)
                    .sortedByDescending { it.time }
                    .map { link -> link.trackId }

            Log.d("track", "$tracksIds")

            appDatabase.trackInPlaylistDao().getTracks(tracksIds)
                .sortedBy { tracksIds.indexOf(it.trackId) }
                .map { trackInPlEntity ->
                    trackDbConverter.map(trackInPlEntity, isFavorite(trackInPlEntity.trackId))
                }
        }

    override fun getPlayLists(): Flow<List<PlayList>> = flow {
        appDatabase.playlistDao().getPlayLists().collect { playListEntities ->
            val playlists = convertFromPlayListEntity(playListEntities)

            for (playlist in playlists) {
                playlist.tracksId.addAll(
                    convertFromTracksInPlaylists(
                        appDatabase.playlistTrackLink().getTracksByPlayListId(playlist.playlistId)
                    )
                )
            }

            emit(playlists)  // Отправляем результат в поток
        }
    }

    override suspend fun getPlaylist(playlistId: Int): PlayList = withContext(Dispatchers.IO) {
        playListDbConverter.map(appDatabase.playlistDao().getPlayList(playlistId))
    }


    //CONVERT
    private fun convertFromPlayListEntity(playList: List<PlayListEntity>): List<PlayList> {
        return playList.map { newPlayList -> playListDbConverter.map(newPlayList) }
    }

    private fun convertFromTracksInPlaylists(tracksInPlayLists: List<PlayListTrackLink>): List<Int> =
        tracksInPlayLists.map { track -> track.trackId }

    //CHECK
    private suspend fun isFavorite(trackId: Int): Boolean =
        withContext(Dispatchers.IO) {
            appDatabase.trackDao().isFavorite(trackId)
        }

    private suspend fun checkAndDelete(trackId: Int) {
        if (appDatabase.playlistTrackLink().getAllByTrackId(trackId).isEmpty())
            appDatabase.trackInPlaylistDao().deleteTrack(trackId)
    }
}