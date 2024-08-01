package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.audioplayer.data.AudioPlayerImpl
import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayer
import com.practicum.playlistmaker.media_favorite.data.FavoriteRepositoryImpl
import com.practicum.playlistmaker.media_favorite.data.db.converters.TrackDbConvert
import com.practicum.playlistmaker.media_favorite.domain.api.FavoriteRepository
import com.practicum.playlistmaker.search.data.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
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
    factory{TrackDbConvert()}
}