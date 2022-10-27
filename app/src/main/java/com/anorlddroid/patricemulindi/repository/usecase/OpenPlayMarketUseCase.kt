package com.anorlddroid.patricemulindi.repository.usecase

import android.app.Activity
import androidx.core.content.ContextCompat.startActivity
import javax.inject.Inject

interface OpenPlayMarketUseCase {
    fun execute(activity: Activity)
}

class OpenPlayMarketUseCaseImpl @Inject constructor() : OpenPlayMarketUseCase {
    override fun execute(activity: Activity) {
        val intent = activity.packageManager.getLaunchIntentForPackage("com.android.vending")
        if (intent != null) {
            startActivity(activity, intent, null)
        }
    }
}