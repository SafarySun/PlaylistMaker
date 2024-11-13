package com.practicum.playlistmaker.media_creation.view_model

import android.net.Uri
import android.util.Log
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


open class PlayListCreationViewModel(
    private val filesInteractor: FileStorageInteractor,
    private val dbInteractor: PlayListCreationInteractor,
) : ViewModel() {

    private val _imageUri = MutableLiveData<String?>()
    val imageUri: LiveData<String?>
        get() = _imageUri

    protected open var  playList = PlayList()
    protected open var  nameEt: String? = null
    protected open var  descriptionEt: String? = null
    protected open var  uriVm: String? = null


    private val completionState = MutableLiveData<CompletionDialogState>()
    fun getCompletionState(): LiveData<CompletionDialogState> = completionState

    fun createPlayList() {
        viewModelScope.launch(Dispatchers.IO) {

            if (uriVm?.isNotEmpty() == true) {
                        Log.e("tag","startupdate")

                Log.e("tag","$uriVm")
                uriVm?.let{
                    filesInteractor.saveImageToPrivateStorage((Uri.parse(uriVm)).toString())
                }
                Log.e("tag","$uriVm")
            }
                dbInteractor.insertPlayList(
                    playList.copy(
                        name = nameEt ?: "",
                        description = descriptionEt ?: "",
                        coverImage = uriVm ?: ""
                    )
                )

            viewModelScope.launch {
                completionState.value = CompletionDialogState.FINISH
            }
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

      open  fun saveImageUri(uri: Uri?) {
            if (uri != null)
                uriVm = uri.toString()
            _imageUri.value = uri.toString()

        }

      open  fun saveNameEt(text: CharSequence?) {
            if (text != null)
                nameEt = text.toString()
        }

      open  fun saveDescriptionEt(text: CharSequence?) {
            if (text != null)
                descriptionEt = text.toString()
        }
    }
