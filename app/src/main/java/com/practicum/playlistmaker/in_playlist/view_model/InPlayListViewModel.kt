package com.practicum.playlistmaker.in_playlist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.in_playlist.view_model.states.MenuState
import com.practicum.playlistmaker.in_playlist.view_model.states.PLDetailsState
import com.practicum.playlistmaker.in_playlist.view_model.states.TrackDetailsState
import com.practicum.playlistmaker.media_creation.domain.api.PlayListCreationInteractor
import com.practicum.playlistmaker.media_creation.domain.api.file_storage.FileStorageInteractor
import com.practicum.playlistmaker.media_creation.domain.model.PlayList
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.utils.ResourceRepository
import com.practicum.playlistmaker.utils.SingleLiveEvent
import com.practicum.playlistmaker.utils.formatDuration
import com.practicum.playlistmaker.utils.getMinutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InPlayListViewModel(
    private val playlistId: Int,
    private val interactor: PlayListCreationInteractor,
    private val resours: ResourceRepository,
    private val sharingInteractor: SharingInteractor,
    private val  fileStorageInteractor : FileStorageInteractor
) : ViewModel() {

    private var trackToDelete: Track? = null

    private val plDetails = MutableLiveData<PLDetailsState>()
    fun observeDetailsPL(): LiveData<PLDetailsState> = plDetails

    private val trackDetails = MutableLiveData<TrackDetailsState>()
    fun observeDetailsTrack(): LiveData<TrackDetailsState> = trackDetails

    private val menuState = MutableLiveData<MenuState>(MenuState.NONE)
    fun observeMenuState(): LiveData<MenuState> = menuState

    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast

    private lateinit var playlist: PlayList
    private val tracks = ArrayList<Track>()

    init {
        fillData()
    }

    private fun fillData() {
        viewModelScope.launch(Dispatchers.IO) {
            playlist = interactor.getPlaylist(playlistId)
            tracks.addAll(interactor.getTracksFromPlayList(playlist))
            getPLDetails()
            getTraksDetails()

        }
    }
    fun onShareClick(amountTracks: String) {
        if (tracks.isEmpty())
            showToast.postValue(resours.getString(R.string.empty_playlist_toast))
        else sharingInteractor.sharePlData(
            createShareMessage(
                playlist,
                tracks,
                amountTracks
            )
        )
    }
    suspend fun playlistDeleteConfirmed() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.deletePlayListById(playlistId)
            fileStorageInteractor.deleteImageFromPrivateStorage(playlist.coverImage)
        }
    }
    fun openMenu(){
        menuState.postValue(MenuState.SHOW)
    }
    fun closeMenu(){
        menuState.postValue(MenuState.NONE)
    }
    private fun getPLDetails() {
        plDetails.postValue(
            PLDetailsState(
                playlist.coverImage,
                playlist.name,
                playlist.description
            )
        )
    }

    private fun getTraksDetails() {
        trackDetails.postValue(
            TrackDetailsState(
                durationTracks = secToMin(),
                playlist.amountTracks,
                tracks
            )
        )
    }
    private fun secToMin(): Int {
        val minutes =  tracks.sumOf { it.trackTimeMillis.toInt() }
        return getMinutes(minutes)
    }
    fun onTrackLongClicked(track: Track) {
        trackToDelete = track
    }

    fun deleteTrack() {
        if (trackToDelete != null){
        viewModelScope.launch(Dispatchers.IO) {
            interactor.deleteTrack(
                trackToDelete!!.trackId,
                playlistId
            )
            tracks.remove(trackToDelete)
            playlist = playlist.copy(amountTracks = playlist.amountTracks - 1)
            trackToDelete = null
            getTraksDetails()
        }}
    }

    fun onResume() {
        viewModelScope.launch {
            playlist = interactor.getPlaylist(playlistId)
            getPLDetails()
        }
    }
    fun createShareMessage(playlist: PlayList, tracks: ArrayList<Track>, amountTracks: String): String {
        val tracksString = buildString {
            append(playlist.name)
            appendLine()
            append(playlist.description)
            appendLine()
            append(amountTracks)
            for (i in 0 until tracks.size) {
                appendLine()
                append("${i + 1}. ${tracks[i].artistName} - ${tracks[i].trackName} ${
                    formatDuration(
                        tracks[i].trackTimeMillis
                    )
                }")
            }
        }
        return tracksString
    }
}