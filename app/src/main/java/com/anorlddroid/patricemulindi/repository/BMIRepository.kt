package com.anorlddroid.patricemulindi.repository

import com.anorlddroid.patricemulindi.model.Details
import com.anorlddroid.patricemulindi.model.Results
import javax.inject.Inject

interface BMIRepository {
    fun convertToDetails(
        name: String,
        weight: Double,
        height: Double,
        gender: String
    ): Details

    fun convertToResults(
        name: String,
        BMIIndex: List<String>,
        PonderalIndex: String,
        category: String,
        categoryInfo: String
    ): Results
}

class BMIRepositoryImpl @Inject constructor() : BMIRepository {
    override fun convertToDetails(
        name: String,
        weight: Double,
        height: Double,
        gender: String
    ) = Details(name, weight, height, gender)

    override fun convertToResults(
        name: String,
        BMIIndex: List<String>,
        PonderalIndex: String,
        category: String,
        categoryInfo: String
    ) = Results(name, BMIIndex, PonderalIndex, category, categoryInfo)

}