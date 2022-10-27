package com.anorlddroid.patricemulindi.repository.usecase

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat
import com.anorlddroid.patricemulindi.repository.ScreenshotRepository
import java.io.File
import javax.inject.Inject


interface ShareScreenshotUseCase {
    fun execute(activity: Activity, imageFile: File)
}

class ShareScreenshotUseCaseImpl @Inject constructor(
    private val mScreenshotRepo: ScreenshotRepository
) : ShareScreenshotUseCase {
    override fun execute(activity: Activity, imageFile: File) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/jpeg"
        shareIntent.putExtra(
            Intent.EXTRA_STREAM,
            mScreenshotRepo.getScreenshotUri(activity, imageFile)
        )
        ContextCompat.startActivity(
            activity,
            Intent.createChooser(shareIntent, "Share Image"),
            null
        )
    }
}