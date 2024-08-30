package com.practicum.playlistmaker.media_creation.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_creation.domain.api.PlayListCreationInteractor
import com.practicum.playlistmaker.media_creation.domain.api.file_storage.FileStorageInteractor
import com.practicum.playlistmaker.media_creation.domain.model.PlayList
import com.practicum.playlistmaker.media_creation.view_model.states.CompletionDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PlayListCreationViewModel(
    private val filesInteractor: FileStorageInteractor,
    private val dbInteractor: PlayListCreationInteractor,
    private var playList: PlayList
) : ViewModel() {

    private val _imageUri = MutableLiveData<String?>()
    val imageUri: LiveData<String?>
        get() = _imageUri


    var nameEt: String? = null
    var descriptionEt: String? = null
    var uriVm: Uri? = null


    private val completionState = MutableLiveData<CompletionDialogState>()
    fun getCompletionState(): LiveData<CompletionDialogState> = completionState

    fun createPlayList() {
        viewModelScope.launch(Dispatchers.IO) {
            val uri = uriVm.toString()
            if (!uri.isNullOrEmpty())
                uriVm?.let { filesInteractor.saveImageToPrivateStorage(it) }

            dbInteractor.insertPlayList(
                playList.copy(
                    name = nameEt ?: "",
                    description = descriptionEt ?: "",
                    coverImage = uriVm.toString() ?: ""
                )
            )
        }
        viewModelScope.launch {
            completionState.value = CompletionDialogState.FINISH
        }
    }



        fun showCompletionDialog() {
            if (!nameEt.isNullOrEmpty() || !_imageUri.value.isNullOrEmpty() || !descriptionEt.isNullOrEmpty()) {
                completionState.value = CompletionDialogState.SHOW_DIALOG
            } else {
                completionState.value = CompletionDialogState.FINISH
            }
        }

        fun continueCreating() {
            completionState.value = CompletionDialogState.CONTINUE
        }

        fun saveImageUri(uri: Uri?) {
            if (uri != null)
                uriVm = uri
            _imageUri.value = uri.toString()

        }

        fun saveNameEt(text: CharSequence?) {
            if (text != null)
                nameEt = text.toString()
        }

        fun saveDescriptionEt(text: CharSequence?) {
            if (text != null)
                descriptionEt = text.toString()
        }
    }
