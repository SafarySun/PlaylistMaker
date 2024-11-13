package com.practicum.playlistmaker.settings.view_model

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    // создаем мутЛД
    private val settingsLiveData = MutableLiveData<Boolean>()

    init {

        settingsLiveData.value = getThemeSettings()

    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        settingsLiveData.value = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun getSettingsLiveData():LiveData<Boolean> = settingsLiveData
    private fun getThemeSettings() =
        settingsInteractor.getThemeSettings()


    fun updateThemeSetting() {
        settingsLiveData.value?.let { settingsInteractor.updateThemeSetting(it) }
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
}