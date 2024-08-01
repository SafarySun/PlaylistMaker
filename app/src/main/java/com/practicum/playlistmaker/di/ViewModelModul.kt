package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.audioplayer.view_model.PlayerViewModel
import com.practicum.playlistmaker.media_favorite.view_model.FavoriteViewModel
import com.practicum.playlistmaker.media_playlist.view_model.PlayListFragmentViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SearchViewModel(trackInteractor = get())
    }

    viewModel {
        SettingsViewModel(sharingInteractor = get(), settingsInteractor = get())
    }

    viewModel { (track: Track) ->

        PlayerViewModel(
            playerInteraсtor = get(),
            track = track,
            favoriteInteractor = get()
        )
    }
    viewModel{
        FavoriteViewModel(favoriteInteractor = get())
    }
    viewModel{
       PlayListFragmentViewModel()
    }
}

