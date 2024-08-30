package com.practicum.playlistmaker.media_creation.data.file_storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

class FileStorageRepositoryImpl(private val context: Context):FileStorageRepository {
    override suspend fun saveImageToPrivateStorage(uri: Uri)  {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "first_cover.jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

    }

   override  suspend fun deleteImageFromPrivateStorage(image:String) {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        file.delete()
    }

}
