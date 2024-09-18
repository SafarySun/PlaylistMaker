package com.practicum.playlistmaker.media_creation.domain.api.file_storage

interface FileStorageInteractor {
    suspend fun saveImageToPrivateStorage(uri: String): String
    suspend fun deleteImageFromPrivateStorage(image:String)
}