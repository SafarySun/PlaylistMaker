package com.practicum.playlistmaker.media_favorite.data.db.converters

import com.practicum.playlistmaker.media_favorite.data.db.entity.TrackEntity
import com.practicum.playlistmaker.search.domain.models.Track

class TrackDbConvert {
    fun map (trackEntity: TrackEntity): Track {
return Track(trackEntity.trackId,
    trackEntity.trackName,
    trackEntity.artistName,
    trackEntity.trackTimeMillis,
    trackEntity.artworkUrl100,
    trackEntity.collectionName,
    trackEntity.releaseDate,
    trackEntity.primaryGenreName,
    trackEntity.country,
    trackEntity.previewUrl,
    isFavorite = true)
    }
    fun map (track: Track): TrackEntity {
        return TrackEntity(track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            timestamp = System.currentTimeMillis())
    }
}