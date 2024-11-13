package com.practicum.playlistmaker.in_playlist.view_model.states

import com.practicum.playlistmaker.search.domain.models.Track

data class TrackDetailsState(
    val durationTracks: Int,
    val amountOfTracks: Int,
    val tracks: List<Track>)
