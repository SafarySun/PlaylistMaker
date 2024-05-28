package com.practicum.playlistmaker.settings.data.impl

import android.content.SharedPreferences
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository


class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) : SettingsRepository {

    //получаем ШП с состоянием темы
    override fun getThemeSettings() = sharedPreferences.getBoolean(PREFERENCES, true)


    //обновляем настройки темы - и применяем изменение к текущеме интерфейсу
    override fun updateThemeSetting(settings: Boolean) {
        sharedPreferences.edit()
            .putBoolean(PREFERENCES, settings)
            .apply()

    }

    companion object {
        const val PREFERENCES = "preferencies"
    }
}