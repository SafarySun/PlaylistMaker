package com.practicum.playlistmaker.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_favorite.domain.api.FavoriteInteractor
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<TrackState>()
    private val showToast = SingleLiveEvent<String?>()
    private var latestSearchText: String = ""
    private var searchJob: Job? = null

    init {
        showHistory()
        viewModelScope.launch{
            favoriteInteractor.getTrack().collect { favoriteTracks -> handleFavoritesChange(favoriteTracks) }
        }
    }
    private  fun handleFavoritesChange(favoriteTracks: List<Track>) {
        val favoriteTrackIds = favoriteTracks.map { it.trackId }.toSet()
        stateLiveData.value?.let { state ->
            if (state is TrackState.Content) {
                val updatedTracks = state.track.map { track ->
                    track.copy(isFavorite = favoriteTrackIds.contains(track.trackId))
                }
                renderState(TrackState.Content(track = updatedTracks))
            }
        }
    }
    fun showHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            trackInteractor
                .getHistory()
                .collect { history ->
                    if (history.isNotEmpty()) {
                        renderState(TrackState.HistoryContent(history))
                    } else {
                        renderState(TrackState.HistoryEmpty)
                    }
                }
        }
    }


    fun addTrackToHistory(track: Track) =
        trackInteractor.addTrackToHistory(track)   // dobavlyaem trek v sharedpref

    fun clearHistory() {
        trackInteractor.clearHistory()
        renderState(TrackState.HistoryEmpty)
    }

    fun observeState(): LiveData<TrackState> = stateLiveData // konvertiruem LD v nemutable
    private fun renderState(state: TrackState) {              //menyaem State
        stateLiveData.postValue(state)
    }

    fun observeShowToast(): LiveData<String?> = showToast      //poluchaem LD for toast
    override fun onCleared() {
        searchJob?.cancel()
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    fun updateSearch() {
        searchRequest(latestSearchText)
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TrackState.Loading)

            viewModelScope.launch {
                trackInteractor
                    .searchTracks(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {

        when {
            errorMessage != null -> {
                renderState(TrackState.Error)
                showToast.postValue(errorMessage)
            }

            foundTracks.isNullOrEmpty() -> {
                renderState(TrackState.Empty)
            }

            else -> {
                renderState(TrackState.Content(track = foundTracks))
            }
        }
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}

