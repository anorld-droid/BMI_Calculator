package com.anorlddroid.patricemulindi.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize



data class Results(
    val name:String,
    val BMIIndex: List<String>,
    val PonderalIndex: String,
    val category: String,
    val categoryInfo: String
)