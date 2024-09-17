package com.practicum.playlistmaker.media_creation.domain.api.file_storage

import android.net.Uri
import java.net.URI

interface FileStorageInteractor {
    suspend fun saveImageToPrivateStorage(uri: Uri): URI
    suspend fun deleteImageFromPrivateStorage(image:String)
}