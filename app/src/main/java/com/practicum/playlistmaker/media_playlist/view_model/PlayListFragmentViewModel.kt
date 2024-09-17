package com.practicum.playlistmaker.media_playlist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_creation.domain.api.PlayListCreationInteractor
import com.practicum.playlistmaker.media_creation.domain.model.PlayList
import kotlinx.coroutines.launch

class PlayListFragmentViewModel(
    private val interactor: PlayListCreationInteractor) : ViewModel() {


    private var playListsState = MutableLiveData<PlayListContentState>()
    fun getPlayListContentState(): LiveData<PlayListContentState> = playListsState



    init {
        fillData()
    }

    fun fillData() {
        viewModelScope.launch {
            interactor
                .getPlaylists()
                .collect { playlist ->
                    processResult(playlist)
                }
        }
    }

    private fun processResult(playlist: List<PlayList>) {
        if (playlist.isEmpty()) {
            renderState(PlayListContentState.Empty)
        } else {
            renderState(PlayListContentState.Content(playlist))
        }
    }


    private fun renderState(state: PlayListContentState) {
        playListsState.postValue(state)
    }
}
