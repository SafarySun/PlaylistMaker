package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>


    // Достаем треки из ШП конвертируем в Обьекты
    fun getHistory() : ArrayList<Track>


    // проверяем обьекты в ШП и добавляем их в историю
    fun addTrackToHistory(track: Track)

    //очищаем историю
    fun clearHistory()
}