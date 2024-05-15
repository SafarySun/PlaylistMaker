package com.practicum.playlistmaker.settings.data.impl

import android.content.Context
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository


class SettingsRepositoryImpl(val context: Context) : SettingsRepository {

    private val sharedPreferences = context.getSharedPreferences(GET, Context.MODE_PRIVATE)
    //получаем ШП с состоянием темы
    override fun getThemeSettings() = sharedPreferences.getBoolean(PREFERENCES, true)


    //обновляем настройки темы - и применяем изменение к текущеме интерфейсу
    override fun updateThemeSetting(settings: Boolean) {
        sharedPreferences.edit()
            .putBoolean(PREFERENCES, settings)
            .apply()

    }

    //меняем тему

    companion object {
        const val PREFERENCES = "preferencies"
        const val GET = "GET"
    }
}