package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.audioplayer.view_model.PlayerViewModel
import com.practicum.playlistmaker.in_playlist.view_model.InPlayListViewModel
import com.practicum.playlistmaker.media_creation.view_model.PlayListCreationViewModel
import com.practicum.playlistmaker.media_edit_info.view_model.PlayListEditInfoViewModel
import com.practicum.playlistmaker.media_favorite.view_model.FavoriteViewModel
import com.practicum.playlistmaker.media_playlist.view_model.PlayListFragmentViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SearchViewModel(
            trackInteractor = get(),
            favoriteInteractor = get()
        )
    }

    viewModel {
        SettingsViewModel(sharingInteractor = get(), settingsInteractor = get())
    }

    viewModel { (track: Track) ->

        PlayerViewModel(
            playlistInteractor = get(),
            playerInteraсtor = get(),
            track = track,
            favoriteInteractor = get()

        )
    }
    viewModel {
        FavoriteViewModel(favoriteInteractor = get())
    }
    viewModel {
        PlayListFragmentViewModel(interactor = get())
    }

    viewModel {
        PlayListCreationViewModel(
            filesInteractor = get(),
            dbInteractor = get(),
        )
    }
    viewModel { (playlistId: Int) ->

       InPlayListViewModel(
            playlistId = playlistId,
           interactor = get(),
           resours = get(),
           sharingInteractor = get(),
           fileStorageInteractor = get()

        )
    }
    viewModel { (playlistId: Int) ->

        PlayListEditInfoViewModel(
            playlistId = playlistId,
            interactor = get(),
            filesInteractor = get(),
            resource = get()

        )
    }

}

