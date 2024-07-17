package com.practicum.playlistmaker.audioplayer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayerInteraсtor
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteraсtor: AudioPlayerInteraсtor,
    private val track: Track,
) : ViewModel() {

    private var timerJob: Job? = null
    private var playerState = MutableLiveData<PlayerState>()
    private var screenState = MutableLiveData<TrackScreenState>(TrackScreenState.Loading)

    init {
        preparePlayer(track.previewUrl)
        screenState.postValue(TrackScreenState.Content(track))

    }

    fun getPlayerState(): LiveData<PlayerState> = playerState
    fun getScreenState(): LiveData<TrackScreenState> = screenState
    private fun renderState(state: PlayerState) {
        playerState.postValue(state)
    }

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

    // nazhatie
    fun playbackControler() {
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
        timerJob = viewModelScope.launch {
            while (playerInteraсtor.isPlaying()) {
                delay(TIMER_ITERATION)
                playerState.postValue(PlayerState.Play(provideCurrentPosition()))
            }
        }
    }

    companion object {
        private const val TIMER_ITERATION = 300L
    }
}






