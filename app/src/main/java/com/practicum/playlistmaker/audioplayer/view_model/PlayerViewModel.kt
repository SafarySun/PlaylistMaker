package com.practicum.playlistmaker.audioplayer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayerInteraсtor
import com.practicum.playlistmaker.media_creation.domain.api.PlayListCreationInteractor
import com.practicum.playlistmaker.media_creation.domain.model.PlayList
import com.practicum.playlistmaker.media_favorite.domain.api.FavoriteInteractor
import com.practicum.playlistmaker.media_playlist.view_model.PlayListContentState
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playlistInteractor: PlayListCreationInteractor,
    private val playerInteraсtor: AudioPlayerInteraсtor,
    private val track: Track,
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {

    private val showToast = SingleLiveEvent<String?>()
    private var timerJob: Job? = null
    private var playerState = MutableLiveData<PlayerState>()
    private var isFavorite = MutableLiveData(track.isFavorite)
    private var screenState = MutableLiveData<TrackScreenState>(TrackScreenState.Loading)
    private val playListState = MutableLiveData<PlayListContentState>()

    private var playLists: List<PlayList> = emptyList()

    init {
        preparePlayer(track.previewUrl)
        screenState.postValue(TrackScreenState.Content(track))
        getPlaylists()
    }

    fun getPlayListState(): LiveData<PlayListContentState> = playListState
    fun getPlayerState(): LiveData<PlayerState> = playerState
    fun getIsFavorite(): LiveData<Boolean> = isFavorite
    fun getScreenState(): LiveData<TrackScreenState> = screenState
    private fun renderState(state: PlayerState) {
        playerState.postValue(state)
    }

    fun observeShowToast(): LiveData<String?> = showToast

    //podgotovka mp
    private fun preparePlayer(previewUrl: String) {
        playerInteraсtor.preparePlayer(
            previewUrl,
            listener = object : AudioPlayerInteraсtor.PlayerListener {

                override fun onPrepared() {
                    renderState(PlayerState.Prepared)
                }

                override fun onCompletion() {
                    renderState(PlayerState.Prepared)
                    timerJob?.cancel()
                }
            })

    }

    fun onFavoriteClicked() {
        isFavorite.value = !track.isFavorite
        viewModelScope.launch(Dispatchers.IO) {
            favoriteInteractor.run {
                if (track.isFavorite) {
                    deleteTrack(track)
                    track.isFavorite = false
                } else {
                    insertTracks(track)
                    track.isFavorite = true
                }
            }
        }
    }


    // nazhatie
    fun playbackController() {
        when (playerState.value) {
            is PlayerState.Play -> {
                pausePlayer()
            }

            PlayerState.Prepared, is PlayerState.Pause -> {
                startPlayer()
            }

            else -> Unit
        }
    }

    private fun reset() {
        playerInteraсtor.reset()
    }

    private fun startPlayer() {
        playerInteraсtor.startPlayer()
        timerStart()
        renderState(PlayerState.Play(provideCurrentPosition()))
    }

    fun pausePlayer() {
        playerInteraсtor.pausePlayer()
        timerJob?.cancel()
        renderState(PlayerState.Pause)

    }


    fun provideCurrentPosition(): Long =
        playerInteraсtor.provideCurrentPosition() //poluchaem timer

    fun isPlaying(): Boolean = playerInteraсtor.isPlaying()  // true or false


    public override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        reset()
        playerState.value = PlayerState.Default

    }

    fun release() = playerInteraсtor.release()
    private fun timerStart() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (playerInteraсtor.isPlaying()) {
                delay(TIMER_ITERATION)
                playerState.postValue(PlayerState.Play(provideCurrentPosition()))
            }
        }
    }

    private fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylist()
                .collect { result ->
                    playLists = result
                }
        }
    }

    fun addTrackButton() {
        renderState(PlayListContentState.Content(playLists))
    }

   /* fun clickOnPlaylist(playlist: PlayList) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("tag","${playlist.tracksId}")
            if (playlist.tracksId.contains(track.trackId)) {
                showToast.postValue("Трек уже добавлен в плейлист ${playlist.name}")

            } else {
                playlist.tracksId.add(track.trackId)
                playlistInteractor.addTrackToPlaylist(
                    track,
                    playlist.copy(amountTracks = playlist.amountTracks + 1)
                )

                withContext(Dispatchers.Main) {
                    showToast.postValue("Добавлено в плейлист ${playlist.name}")
                    playListState.postValue(PlayListContentState.Empty)
                }
            }
        }
    }

    */
   fun clickOnPlaylist(playlist: PlayList) {
       viewModelScope.launch(Dispatchers.IO) {
          val addTrack = playlistInteractor.addTrackToPlaylist(track,playlist)
           if (addTrack) {
               showToast.postValue("Добавлено в плейлист ${playlist.name}")
               playListState.postValue(PlayListContentState.Empty)
           } else {
               showToast.postValue("Трек уже добавлен в плейлист ${playlist.name}")
           }
       }
   }


    private fun renderState(state: PlayListContentState) {
        playListState.postValue(state)
    }

    companion object {
        private const val TIMER_ITERATION = 300L
    }
}






