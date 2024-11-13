package com.practicum.playlistmaker.media_favorite.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_favorite.domain.api.FavoriteInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val favoriteInteractor: FavoriteInteractor) : ViewModel() {

    private var contentState = MutableLiveData<FavoriteContentState>()
    fun getContentState(): LiveData<FavoriteContentState> = contentState

    init {
        fillData()
    }
    fun fillData() {
        viewModelScope.launch {
            favoriteInteractor
                .getTrack()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }

    private fun processResult(track: List<Track>) {
        if (track.isEmpty()) {
            renderState(FavoriteContentState.Empty)
        } else {
            renderState(FavoriteContentState.ContentState(track))
        }
    }

    private fun renderState(state: FavoriteContentState) {
        contentState.postValue(state)
    }

}