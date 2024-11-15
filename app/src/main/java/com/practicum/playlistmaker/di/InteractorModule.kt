package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayerInteraсtor
import com.practicum.playlistmaker.audioplayer.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.media_creation.domain.api.PlayListCreationInteractor
import com.practicum.playlistmaker.media_creation.domain.api.file_storage.FileStorageInteractor
import com.practicum.playlistmaker.media_creation.domain.impl.FileStorageInteractorImpl
import com.practicum.playlistmaker.media_creation.domain.impl.PlayListCreationInteractorImpl
import com.practicum.playlistmaker.media_favorite.domain.api.FavoriteInteractor
import com.practicum.playlistmaker.media_favorite.domain.impl.FavoriteInteractorImpl
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<TrackInteractor> {
        TrackInteractorImpl(repository = get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(externalNavigator = get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(settingsRepository = get())
    }

    single<AudioPlayerInteraсtor> {
        AudioPlayerInteractorImpl(player = get())
    }
    single<FavoriteInteractor> {
        FavoriteInteractorImpl(favoriteRepository = get())
    }
    single<FileStorageInteractor> {
       FileStorageInteractorImpl(fileStorageRepository = get())
    }
    single<PlayListCreationInteractor> {
       PlayListCreationInteractorImpl(repository =  get())
    }
}