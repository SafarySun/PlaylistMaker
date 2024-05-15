package com.practicum.playlistmaker.audioplayer.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.audioplayer.domain.api.PlayerListern
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.creator.Creator

class PlayerViewModel( val track: Track, previewUrl: String, listener: PlayerListern) : ViewModel() {

    private val timerRunnable: Runnable = object : Runnable {
        override fun run() {
                playerState.postValue(PlayerState.Play(provideCurrentPosition()))
                mainThreadHandler.postDelayed(this, 300)
            }
        }

    private val interactorImpl = Creator.getInteractorPlayer()

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private var playerState = MutableLiveData<PlayerState>()

    private var screenState = MutableLiveData<TrackScreenState>(TrackScreenState.Loading)

    init {
            preparePlayer(previewUrl, listener)
            screenState.postValue(TrackScreenState.Content(track))

    }

    fun getPlayerState(): LiveData<PlayerState> = playerState
    fun getScreenState(): LiveData<TrackScreenState> = screenState
    private fun renderState(state: PlayerState) {
        playerState.postValue(state)
    }

    //podgotovka mp
    private fun preparePlayer(previewUrl: String, listner: PlayerListern) {
        interactorImpl.preparePlayer(previewUrl, listner)
        renderState(PlayerState.Prepared)
    }

    // nazhatie
    fun playbackControler() {
        when (playerState.value) {
            is PlayerState.Play -> {
                mainThreadHandler.removeCallbacks(timerRunnable)
                pausePlayer()
            }

            PlayerState.Prepared, PlayerState.Pause -> {
                startPlayer()
                mainThreadHandler.post(timerRunnable)
            }

            else -> Unit
        }
    }
    fun reset(){
        interactorImpl.reset()
    }

    private fun startPlayer() {               //  MP  start
        interactorImpl.startPlayer()
        renderState(PlayerState.Play(provideCurrentPosition()))
    }

    private fun pausePlayer() {
       // MP  pauza
            interactorImpl.pausePlayer()
            renderState(PlayerState.Pause)

    }


    fun provideCurrentPosition(): Long =
        interactorImpl.provideCurrentPosition().toLong() //poluchaem timer

    fun isPlaying(): Boolean = interactorImpl.isPlaying()  // true or false


    override fun onCleared() {
        super.onCleared()
        interactorImpl.reset()
        playerState.value = PlayerState.Default
    }
    fun release() = interactorImpl.release()

    companion object {
        fun getViewModelFactory(
            track: Track,
            previewUrl: String,
            listner: PlayerListern,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(track ,previewUrl, listner)
            }
        }
    }
}



