package com.practicum.playlistmaker.settings.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.utils.creator.Creator

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    // создаем мутЛД
    private val settingsLiveData = MutableLiveData<Boolean>()

    init {
        settingsLiveData.value = getThemeSettings()
    }

    fun getSettingsLiveData():LiveData<Boolean> = settingsLiveData
    fun getThemeSettings() =
        settingsInteractor.getThemeSettings().darkTheme


    fun updateThemeSetting(isCheked:Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(isCheked))
    }
    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

   fun openSupport() {
       sharingInteractor.openSupport()
    }

    companion object {
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val providerSettings = Creator.getSettingsInteractor(context)
                val providerSharing = Creator.getSharingInteractor(context)
                SettingsViewModel(
                    providerSharing,
                    providerSettings
                )
            }
        }
    }
}