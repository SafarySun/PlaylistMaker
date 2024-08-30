package com.practicum.playlistmaker.media_creation.data.file_storage

import android.net.Uri

interface FileStorageRepository {

    suspend fun saveImageToPrivateStorage(uri: Uri)
    suspend fun deleteImageFromPrivateStorage(image:String)
}