package com.practicum.playlistmaker.media_creation.data.file_storage

import android.net.Uri
import java.net.URI

interface FileStorageRepository {

    suspend fun saveImageToPrivateStorage(uri: Uri): URI
    suspend fun deleteImageFromPrivateStorage(image:String)
}