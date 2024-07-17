package com.practicum.playlistmaker.search.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.model.TrackState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackInteractor: TrackInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<TrackState>()
    private val handler = Handler(Looper.getMainLooper())
    private val showToast = SingleLiveEvent<String?>()
    private var latestSearchText: String = ""
    private var searchJob: Job? = null

    init {

        if (getHistory().isNotEmpty()) {
            renderState(TrackState.HistoryContent(getHistory()))
        } else {
            renderState(TrackState.HistoryEmpty)
        }
    }

    fun onClearTextClick(show: () -> Unit, empty: () -> Unit) {
        val history = getHistory()
        if (history.isNotEmpty()) {
            renderState(TrackState.HistoryContent(history))
            show.invoke()
        } else {
            renderState(TrackState.HistoryEmpty)
            empty.invoke()
        }
    }

    fun getHistory(): List<Track> = trackInteractor.getHistory()  // polu4aem treki iz sharedpref


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

        val track = mutableListOf<Track>()

        if (foundTracks != null) {
            track.addAll(foundTracks)
        }else{
            renderState(TrackState.Empty)
        }

        when {
            errorMessage != null -> {
                renderState(TrackState.Error)
                showToast.postValue(errorMessage)
            }

            track.isEmpty() -> {
                renderState(TrackState.Empty)
            }

            else -> {
                renderState(TrackState.Content(track = track))
            }
        }
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}

