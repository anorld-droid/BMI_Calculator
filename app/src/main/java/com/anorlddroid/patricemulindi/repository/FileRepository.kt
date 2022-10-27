package com.anorlddroid.patricemulindi.repository

import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import com.anorlddroid.patricemulindi.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject


interface FileRepository {
    fun saveImageFile(activity: Activity, bitmap: Bitmap): File
}

class FileRepositoryImpl @Inject constructor() : FileRepository {
    override fun saveImageFile(activity: Activity, bitmap: Bitmap): File {
        val file: File? = activity.externalCacheDir
        val date = Date()
        android.text.format.DateFormat.format("yyyy/MM/dd-hh:mm:ss", date)
        val name = date.toString().replace("\\s".toRegex(), "")
        val imageFile =
            File(file, "$name.jpg")
        try {
            val outputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (ex: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e("Screenshot", "Exception: $ex ")
            }
        }
        return imageFile
    }
}