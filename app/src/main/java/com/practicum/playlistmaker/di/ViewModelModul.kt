package com.practicum.playlistmaker.di

import android.util.Log
import com.practicum.playlistmaker.audioplayer.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        try {
            SearchViewModel(trackInteractor = get())
        } catch (e: Exception) {
            Log.e("Koin", "Error creating SearchViewModel", e)
            throw e
        }
    }

    viewModel {
        try {
            SettingsViewModel(sharingInteractor = get(), settingsInteractor = get())
        } catch (e: Exception) {
            Log.e("Koin", "Error creating SettingsViewModel", e)
            throw e
        }
    }

    viewModel { (track: Track) ->
        try {
            PlayerViewModel(
                playerInteraсtor = get(),
                track = track
            )
        } catch (e: Exception) {
            Log.e("Koin", "Error creating PlayerViewModel", e)
            throw e
        }
    }
}