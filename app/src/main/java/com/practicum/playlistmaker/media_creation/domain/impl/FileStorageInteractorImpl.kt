package com.practicum.playlistmaker.media_creation.domain.impl

import android.net.Uri
import com.practicum.playlistmaker.media_creation.data.file_storage.FileStorageRepository
import com.practicum.playlistmaker.media_creation.domain.api.file_storage.FileStorageInteractor

class FileStorageInteractorImpl(private val fileStorageRepository: FileStorageRepository) :
    FileStorageInteractor {
    override suspend fun saveImageToPrivateStorage(uri: Uri) {
        return fileStorageRepository.saveImageToPrivateStorage(uri)
    }
    override suspend fun deleteImageFromPrivateStorage(image: String){
        fileStorageRepository.deleteImageFromPrivateStorage(image)
    }
}