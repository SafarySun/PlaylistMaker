package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>

    // Достаем треки из ШП конвертируем в Обьекты
    fun getHistory() : ArrayList<Track>

    // проверяем обьекты в ШП и добавляем их в историю
    fun addTrackToHistory(track: Track)

    //очищаем историю
    fun clearHistory()

    //interface TrackConsumer {
    //    fun consume(foundTracks: List<Track>?,errorMessage:String?)
   // }
}