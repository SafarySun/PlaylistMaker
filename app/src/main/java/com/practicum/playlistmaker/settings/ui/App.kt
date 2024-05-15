package com.practicum.playlistmaker.settings.ui

import android.app.Application
import com.practicum.playlistmaker.utils.creator.Creator

class App : Application() {


    override fun onCreate() {
        super.onCreate()

        Creator.setApplicationValue(this)

    }

}