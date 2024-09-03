package com.practicum.playlistmaker.media_creation.domain.impl

import com.practicum.playlistmaker.media_creation.domain.api.PlayListCreationInteractor
import com.practicum.playlistmaker.media_creation.domain.api.PlayListCreationRepository
import com.practicum.playlistmaker.media_creation.domain.model.PlayList
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlayListCreationInteractorImpl(
    private val repository: PlayListCreationRepository
) : PlayListCreationInteractor {
    override suspend fun insertPlayList(playlist: PlayList) {
        repository.insertPlayList(playlist)
    }

    override suspend fun deletePlaylist(playlist: PlayList) {
        repository.deletePlaylist(playlist)
    }

    override fun getPlaylist(): Flow<List<PlayList>> {
        return repository.getPlayLists()
    }

    override suspend fun getPlaylist(playlistId: Int): PlayList =
        repository.getPlaylist(playlistId)


    override suspend fun addTrackToPlaylist(track: Track, playlist: PlayList) : Boolean =
        repository.addTrackToPlaylist(track, playlist)
}