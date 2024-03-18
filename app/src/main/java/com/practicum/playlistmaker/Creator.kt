package com.practicum.playlistmaker

import com.practicum.playlistmaker.audioplayer.data.AudioPlayerImpl
import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayer
import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayerInteraktor
import com.practicum.playlistmaker.audioplayer.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.search.data.TrackRepositoryImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.impl.TrackInteractorImpl

object Creator {
    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }
    private fun getMediaplayer(): AudioPlayer{
        return AudioPlayerImpl()
    }
    fun getInteractorPlayer(): AudioPlayerInteraktor{
        return AudioPlayerInteractorImpl(getMediaplayer())

    }
}