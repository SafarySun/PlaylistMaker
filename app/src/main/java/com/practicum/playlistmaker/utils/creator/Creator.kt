package com.practicum.playlistmaker.utils.creator

import android.app.Application
import android.content.Context
import com.practicum.playlistmaker.audioplayer.data.AudioPlayerImpl
import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayer
import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayerInteraktor
import com.practicum.playlistmaker.audioplayer.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.search.data.TrackRepositoryImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.sharedprefs.LocalStorage
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {
    lateinit var application: Application
    fun setApplicationValue(app: Application) {
        application = app
    }


    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(
            RetrofitNetworkClient(context),
            LocalStorage(context),
            context
        )
    }

    fun provideTrackInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(context))
    }

    private fun getMediaplayer(): AudioPlayer {
        return AudioPlayerImpl()
    }

    fun getInteractorPlayer(): AudioPlayerInteraktor {
        return AudioPlayerInteractorImpl(getMediaplayer())
    }

    private fun getSettingsRepository(context:Context):SettingsRepository{
        return SettingsRepositoryImpl(context)
    }
    fun getSettingsInteractor(context: Context):SettingsInteractor{
        return SettingsInteractorImpl(getSettingsRepository(context))
    }
    private fun getExternalNavigator(context:Context):ExternalNavigator{
        return ExternalNavigatorImpl(context)
    }
    fun getSharingInteractor(context: Context):SharingInteractor{
        return SharingInteractorImpl(getExternalNavigator(context))
    }
}