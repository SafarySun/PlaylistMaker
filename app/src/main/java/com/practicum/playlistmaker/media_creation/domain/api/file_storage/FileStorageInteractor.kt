package com.practicum.playlistmaker.media_creation.domain.api.file_storage

import android.net.Uri

interface FileStorageInteractor {
    suspend fun saveImageToPrivateStorage(uri: Uri)
    suspend fun deleteImageFromPrivateStorage(image:String)
}