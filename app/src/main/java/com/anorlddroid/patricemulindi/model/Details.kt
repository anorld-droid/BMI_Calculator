package com.anorlddroid.patricemulindi.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Details(
    val name: String,
    val weight: Double,
    val height: Double,
    val gender: String
) : Parcelable
