package com.practicum.playlistmaker.utils

interface ResourceRepository {
    fun getString(resId: Int): String
}