package com.practicum.playlistmaker.utils

import android.content.Context

class ResourceRepositoryImpl(private val context: Context) : ResourceRepository {
    override fun getString(resId: Int) = context.getString(resId)
}