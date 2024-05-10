package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface TrackInteractor {
    fun searchTracks(expression: String, consumer: TrackConsumer)

    // Достаем треки из ШП конвертируем в Обьекты
    fun getHistory() : ArrayList<Track>

    // проверяем обьекты в ШП и добавляем их в историю
    fun addTrackToHistory(track: Track)

    //очищаем историю
    fun clearHistory()
    interface TrackConsumer {
        fun consume(foundTracks: List<Track>?,errorMessage:String?)
    }
}