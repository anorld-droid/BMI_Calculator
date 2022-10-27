package com.anorlddroid.patricemulindi.repository.usecase

import android.app.Activity
import androidx.core.content.ContextCompat.startActivity
import javax.inject.Inject

interface OPenPlayMarketUseCase {
    fun execute(activity: Activity)
}

class OPenPlayMarketUseCaseImpl @Inject constructor() : OPenPlayMarketUseCase{
    override fun execute(activity: Activity) {
        val intent = activity.packageManager.getLaunchIntentForPackage("com.android.vending")
        if (intent != null) {
            startActivity(activity, intent, null)
        }
    }
}