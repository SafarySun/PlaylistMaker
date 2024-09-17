package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.audioplayer.data.AudioPlayerImpl
import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayer
import com.practicum.playlistmaker.converters.PlayListDbConverts
import com.practicum.playlistmaker.converters.TrackDbConvert
import com.practicum.playlistmaker.media_creation.data.db.repository.PlayListCreationRepositoryImpl
import com.practicum.playlistmaker.media_creation.data.file_storage.FileStorageRepository
import com.practicum.playlistmaker.media_creation.data.file_storage.FileStorageRepositoryImpl
import com.practicum.playlistmaker.media_creation.domain.api.PlayListCreationRepository
import com.practicum.playlistmaker.media_favorite.data.FavoriteRepositoryImpl
import com.practicum.playlistmaker.media_favorite.domain.api.FavoriteRepository
import com.practicum.playlistmaker.search.data.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker.utils.ResourceRepository
import com.practicum.playlistmaker.utils.ResourceRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(
            networkClient = get(),
            sharedPreferences = get(),
            context = androidContext(),
            appDatabase = get()
        )
    }
    single<SettingsRepository> {
        SettingsRepositoryImpl(sharedPreferences = get(named("settings")))
    }
    single<AudioPlayer> {
        AudioPlayerImpl(get())
    }
    single<FavoriteRepository>{
        FavoriteRepositoryImpl(appDatabase = get(), trackDbConvert = get())
    }
    factory{ TrackDbConvert() }

    single<FileStorageRepository>{
        FileStorageRepositoryImpl(context = androidContext())
    }
    single<PlayListCreationRepository> {
        PlayListCreationRepositoryImpl(
            appDatabase = get(),
            playListDbConverter = get(),
        trackDbConverter = get())
    }
    factory{ PlayListDbConverts() }

    single<ResourceRepository>{
        ResourceRepositoryImpl(context = get())
    }
}
