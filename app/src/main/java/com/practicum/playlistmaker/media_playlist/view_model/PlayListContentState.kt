package com.practicum.playlistmaker.media_playlist.view_model

import com.practicum.playlistmaker.media_creation.domain.model.PlayList

sealed interface PlayListContentState {
   data class Content(val playlists:List<PlayList>): PlayListContentState

    object Empty : PlayListContentState


}