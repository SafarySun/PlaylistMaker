package com.practicum.playlistmaker.media_favorite.view_model

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface FavoriteContentState {
   data class ContentState(val track: List<Track>):FavoriteContentState
    object Empty : FavoriteContentState

}