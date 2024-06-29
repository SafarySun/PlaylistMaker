package com.practicum.playlistmaker.audioplayer.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.audioplayer.domain.api.AudioPlayerInteraсtor
import com.practicum.playlistmaker.search.domain.models.Track

class PlayerViewModel(
    private val playerInteraсtor: AudioPlayerInteraсtor,
    private val track: Track,
) : ViewModel() {

    private val timerRunnable: Runnable = object : Runnable {
        override fun run() {
            playerState.postValue(PlayerState.Play(provideCurrentPosition()))
            mainThreadHandler.postDelayed(this, 300)
        }
    }

    private val mainThreadHandler = Handler(Looper.getMainLooper())

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
                }
            })

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

    private fun reset() {
        playerInteraсtor.reset()
    }

     fun startPlayer() {
        //  MP  start
        playerInteraсtor.startPlayer()
        renderState(PlayerState.Play(provideCurrentPosition()))
    }

     fun pausePlayer() {               // MP  pauza
        playerInteraсtor.pausePlayer()
        renderState(PlayerState.Pause)

    }


    fun provideCurrentPosition(): Long =
        playerInteraсtor.provideCurrentPosition() //poluchaem timer

    fun isPlaying(): Boolean = playerInteraсtor.isPlaying()  // true or false


    override fun onCleared() {
        super.onCleared()
        reset()
        playerState.value = PlayerState.Default
    }

    fun release() = playerInteraсtor.release()
}





