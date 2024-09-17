package com.practicum.playlistmaker.media_edit_info.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.in_playlist.view_model.states.PLDetailsState
import com.practicum.playlistmaker.media_creation.domain.api.PlayListCreationInteractor
import com.practicum.playlistmaker.media_creation.domain.api.file_storage.FileStorageInteractor
import com.practicum.playlistmaker.media_creation.domain.model.PlayList
import com.practicum.playlistmaker.media_creation.view_model.PlayListCreationViewModel
import com.practicum.playlistmaker.media_edit_info.view_model.state.TitleAndButtonState
import com.practicum.playlistmaker.utils.ResourceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayListEditInfoViewModel(
    filesInteractor: FileStorageInteractor,
    private val interactor: PlayListCreationInteractor,
    private val playlistId: Int,
    private val resource: ResourceRepository,
    ) : PlayListCreationViewModel(filesInteractor,interactor) {

    override lateinit var playList: PlayList

    private val plDetails = MutableLiveData<PLDetailsState>()
    fun observeDetailsPL(): LiveData<PLDetailsState> = plDetails

        private val titleAndButton = MutableLiveData<TitleAndButtonState>()

        fun observeTitleAndButtonState(): LiveData<TitleAndButtonState> =
            titleAndButton

        fun initScreen() {
            viewModelScope.launch {
                playList = getPlayList(playlistId)
                plDetails.value =
                    PLDetailsState(playList.coverImage, playList.name,playList.description)
                titleAndButton.value = TitleAndButtonState(
                    resource.getString(R.string.edit_title),
                    resource.getString(R.string.save)
                )
                nameEt = playList.name
                descriptionEt = playList.description
                uriVm = playList.coverImage
            }
        }

        private suspend fun getPlayList(playlistId: Int): PlayList = withContext(Dispatchers.IO) {
            interactor.getPlaylist(playlistId)
        }

        override fun saveNameEt(text: CharSequence?) {
            if (text != null) {
                playList = playList.copy(name = text.toString())
            }
            super.saveNameEt(text)
        }

        override fun saveDescriptionEt(text: CharSequence?) {
            if (text != null) {
                playList = playList.copy(description = text.toString())
            }
            super.saveDescriptionEt(text)
        }
    }

