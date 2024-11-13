package com.practicum.playlistmaker.media_creation.data.file_storage

interface FileStorageRepository {

    suspend fun saveImageToPrivateStorage(uri:String):String
    suspend fun deleteImageFromPrivateStorage(image:String)
}