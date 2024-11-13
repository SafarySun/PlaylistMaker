package com.practicum.playlistmaker.search.data.sharedprefs

import com.practicum.playlistmaker.search.domain.models.Track

interface LocalStorage {
    fun loadTracks() : ArrayList<Track>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
}