package com.practicum.playlistmaker.media_creation.data.file_storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class FileStorageRepositoryImpl(private val context: Context):FileStorageRepository {
    override suspend fun saveImageToPrivateStorage(uri: String):String  =

        withContext(Dispatchers.IO) {
            val filePath =
                File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
            if (!filePath.exists()) {
                filePath.mkdirs()
            }
            val file = File(filePath, uri.substringAfterLast('/'))

            Log.e("tag","$file")

            if (!file.exists()) {
                file.createNewFile()
            }
            val inputStream = context.contentResolver.openInputStream(uri.toUri())
            val outputStream =
                FileOutputStream(file)
            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
Log.e("tag","$file")
            file.toUri().toString()


        }

   override  suspend fun deleteImageFromPrivateStorage(image:String) {
       val file = File((Uri.parse(image)).path.toString())
        file.delete()
    }

}
