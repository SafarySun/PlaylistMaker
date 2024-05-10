package com.practicum.playlistmaker.settings.data.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings


class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(GET, Context.MODE_PRIVATE)
    }
    //получаем ШП с состоянием темы
    override fun getThemeSettings() =
        ThemeSettings(sharedPreferences.getBoolean(PREFERENCES, false))

            //обновляем настройки темы - и применяем изменение к текущеме интерфейсу
    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit()
            .putBoolean(PREFERENCES, settings.darkTheme)
            .apply()
        switchTheme()
    }

    //меняем тему
    override fun switchTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (getThemeSettings().darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val PREFERENCES = "preferencies"
        const val GET = "GET"
    }
}