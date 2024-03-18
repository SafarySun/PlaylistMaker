package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.TrackRequest
import com.practicum.playlistmaker.search.data.dto.TrackResponse
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackRequest(expression))
        if (response.resultCode == 200) {
            return (response as TrackResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
        } else {
            return emptyList()
        }
    }
}

