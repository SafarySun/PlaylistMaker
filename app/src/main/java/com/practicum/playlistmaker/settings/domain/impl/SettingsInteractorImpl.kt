package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository):
    SettingsInteractor {
    override fun getThemeSettings() =
        settingsRepository.getThemeSettings()


    override fun updateThemeSetting(settings: Boolean) {
        settingsRepository.updateThemeSetting(settings)
    }

}