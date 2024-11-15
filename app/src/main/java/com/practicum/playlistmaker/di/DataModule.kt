package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.media_creation.domain.model.PlayList
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.network.PlayListApi
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.sharedprefs.LocalStorage
import com.practicum.playlistmaker.search.data.sharedprefs.LocalStorageImpl
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.utils.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<PlayListApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlayListApi::class.java)
    }

    single(named("history")) {
        androidContext()
            .getSharedPreferences("history_local_storage", Context.MODE_PRIVATE)
    }

    single(named("settings")) {
        androidContext()
            .getSharedPreferences("settings_local_storage", Context.MODE_PRIVATE)
    }
    factory { Gson() }

    single<LocalStorage> {
        LocalStorageImpl(sharedPreferences = get(named("history")), gson = get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(androidContext(), playListApi = get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    factory { MediaPlayer() }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    factory { PlayList() }

}
