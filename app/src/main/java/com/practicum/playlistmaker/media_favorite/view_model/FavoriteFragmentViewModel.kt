package com.practicum.playlistmaker.media_favorite.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.domain.models.Track

class FavoriteFragmentViewModel : ViewModel() {
    private val tracks = arrayListOf<Track>()

    private var contentState = MutableLiveData<FavoriteContentState>()
    fun getContentState(): LiveData<FavoriteContentState> = contentState

    init {
        if (tracks.isNullOrEmpty()) renderState(FavoriteContentState.EMPTY)
        else renderState(FavoriteContentState.CONTENT)
    }

    private fun renderState(state: FavoriteContentState) {
        contentState.postValue(state)
    }
}