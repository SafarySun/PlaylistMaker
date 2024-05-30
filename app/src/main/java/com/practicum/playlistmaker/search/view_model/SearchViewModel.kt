package com.practicum.playlistmaker.search.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.model.TrackState

class SearchViewModel(
    private val trackInteractor: TrackInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<TrackState>()
    private val handler = Handler(Looper.getMainLooper())
    private val showToast = SingleLiveEvent<String?>()
    private var latestSearchText: String = ""

    init {

        if (getHistory().isNotEmpty()) {
            renderState(TrackState.HistoryContent(getHistory()))
        } else {
            renderState(TrackState.HistoryEmpty)
        }
    }
    fun onClearTextClick(show: () -> Unit,empty: () -> Unit){
        val history = getHistory()
        if(history.isNotEmpty()) {
            renderState(TrackState.HistoryContent(history))
            show.invoke()
        }else{
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
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun updateSearch() {
        searchRequest(latestSearchText)
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TrackState.Loading)

            trackInteractor.searchTracks(newSearchText, object : TrackInteractor.TrackConsumer {
                override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                    val track = mutableListOf<Track>()
                    if (foundTracks != null) {
                        track.addAll(foundTracks)
                    }

                    when {
                        errorMessage != null -> {
                            renderState(
                                TrackState.Error
                            )
                            showToast.postValue(errorMessage)
                        }
                        track.isEmpty() -> {
                            renderState(
                                TrackState.Empty
                            )
                        }

                        else-> {
                            renderState(
                                TrackState.Content(
                                    track = track
                                )
                            )
                        }

                    }
                }
            })
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

    }
}
