package com.practicum.playlistmaker.audioplayer.view_model

import com.practicum.playlistmaker.search.domain.models.Track

sealed class TrackScreenState  {
     object Loading:TrackScreenState()
     data class Content(val track : Track) :TrackScreenState()

 }
