package com.practicum.playlistmaker.search.ui.model

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface TrackState {

    object Loading : TrackState

    data class Content(
        val track: List<Track>
    ) : TrackState

    data class HistoryContent(
        val track: List<Track>
    ) : TrackState

    object HistoryEmpty : TrackState

    object Error : TrackState

    object Empty : TrackState

}