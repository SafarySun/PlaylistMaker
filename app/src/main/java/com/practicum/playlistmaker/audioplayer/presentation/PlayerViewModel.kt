package com.practicum.playlistmaker.audioplayer.presentation

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.audioplayer.domain.PlayerListern
import com.practicum.playlistmaker.utils.creator.Creator

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var timerRunnable: Runnable
    private val interactorImpl = Creator.getInteractorPlayer()
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val playerState = MutableLiveData<PlayerState>()


    init {
        setupTimer() // Инициализация timerRunnable при создании ViewModel
    }

    fun getPlayerState(): LiveData<PlayerState> = playerState
    private fun renderState(state: PlayerState) {
        playerState.postValue(state)
    }

    //podgotovka mp
    fun preparePlayer(previewUrl: String, listner: PlayerListern) {
        interactorImpl.preparePlayer(previewUrl, listner)
        renderState(PlayerState.STATE_PREPARED)
    }

    // nazhatie
    fun playbackControler() {
        when (playerState.value) {
            PlayerState.STATE_PLAYING -> {
                mainThreadHandler.removeCallbacks(timerRunnable)
                pausePlayer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
                mainThreadHandler.post(timerRunnable)
            }

            else -> Unit
        }
    }

    fun startPlayer() {               //  MP  start
        interactorImpl.startPlayer()
        renderState(PlayerState.STATE_PLAYING)
    }

    fun pausePlayer() {               // MP  pauza
        interactorImpl.pausePlayer()
        renderState(PlayerState.STATE_PAUSED)
    }


    fun release() = interactorImpl.release() // udalenie mp

    fun provideCurrentPosition(): Int = interactorImpl.provideCurrentPosition() //poluchaem timer

    fun isPlaying(): Boolean = interactorImpl.isPlaying()  // true or false

    fun setupTimer() {
        timerRunnable = Runnable {
            if (interactorImpl.isPlaying()) {
                mainThreadHandler.postDelayed(timerRunnable, 300)
            }
        }
    }
    override fun onCleared() {
        interactorImpl.release()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }
}



