package com.anorlddroid.patricemulindi.repository

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.view.View
import androidx.core.content.FileProvider
import com.anorlddroid.patricemulindi.BuildConfig
import java.io.File
import javax.inject.Inject

interface ScreenshotRepository {
    fun takeScreenshot(view: View): Bitmap
    fun getScreenshotUri(activity: Activity, imageFile: File): Uri
}


class ScreenshotRepositoryImpl @Inject constructor() : ScreenshotRepository {

    override fun takeScreenshot(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return bitmap
    }

    override fun getScreenshotUri(activity: Activity, imageFile: File): Uri {

        return FileProvider.getUriForFile(
            activity,
            BuildConfig.APPLICATION_ID + "." + activity.localClassName + ".provider",
            imageFile
        )
    }
}