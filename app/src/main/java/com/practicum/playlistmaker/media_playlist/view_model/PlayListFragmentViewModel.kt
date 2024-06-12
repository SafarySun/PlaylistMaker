package com.practicum.playlistmaker.media_playlist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.domain.models.Track

class PlayListFragmentViewModel : ViewModel() {

    private val tracks = arrayListOf<Track>()

    private var playListsState = MutableLiveData<PlayListContentState>()
     fun getPlayListContentState(): LiveData<PlayListContentState> = playListsState

    init {
        if (tracks.isNullOrEmpty()) renderState(PlayListContentState.EMPTY)
        else renderState(PlayListContentState.CONTENT)
    }

    private fun renderState(state: PlayListContentState) {
        playListsState.postValue(state)
    }
}
