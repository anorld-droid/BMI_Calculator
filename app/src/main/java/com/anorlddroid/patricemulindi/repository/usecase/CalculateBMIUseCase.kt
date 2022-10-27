package com.anorlddroid.patricemulindi.repository.usecase

import android.content.Context
import android.content.res.Resources
import com.anorlddroid.patricemulindi.R
import com.anorlddroid.patricemulindi.model.Details
import com.anorlddroid.patricemulindi.model.Results
import com.anorlddroid.patricemulindi.repository.BMIRepository
import javax.inject.Inject

interface CalculateBMIUseCase {
    fun calculateBMI(weight: Double, height: Double): Double
    fun calculatePonderalIndex(weight: Double, height: Double): Double
    fun getCategory(mBMI: Double, context: Context): Map<String, String>
    fun execute(userDetails: Details, context: Context): Results

}

class CalculateBMIUseCaseImpl @Inject constructor(
    private val mBMIRepo: BMIRepository
) : CalculateBMIUseCase {
    override fun execute(userDetails: Details, context: Context): Results {
        val bmi: Double = calculateBMI(userDetails.weight, userDetails.height)

        val ponderal: Double = calculatePonderalIndex(userDetails.weight, userDetails.height)
        val categoryDetails: Map<String, String> = getCategory(bmi, context)
        val category: String = categoryDetails[context.getString(R.string.category)]!!

        val categoryInfo = categoryDetails[context.getString(R.string.categoryInfo)]!!
        val bmIndex = String.format("%.2f", bmi)

        return mBMIRepo.convertToResults(
            name = userDetails.name,
            BMIIndex = bmIndex.split("."),
            PonderalIndex = context.getString(R.string.p_index, ponderal),
            category = category,
            categoryInfo = categoryInfo
        )
    }

    override fun calculateBMI(weight: Double, height: Double) = weight / ((height * height) / 10000)

    override fun calculatePonderalIndex(weight: Double, height: Double) =
        weight / ((height * height * height) / 1000000)

    override fun getCategory(mBMI: Double, context: Context): Map<String, String> {
        val category: String
        val categoryInfo: String
        val categoryDetails: MutableMap<String, String> = mutableMapOf()

        when (mBMI) {
            in 0.0..18.4 -> {
                categoryInfo = context.getString(R.string.underweight_info)
                category = context.getString(R.string.category)
            }
            in 18.5..24.9 -> {
                categoryInfo = context.getString(R.string.normal_info)
                category = context.getString(R.string.normal)
            }
            in 25.0..29.9 -> {
                categoryInfo = context.getString(R.string.overweight_info)
                category = context.getString(R.string.overweight)
            }
            in 30.0..39.9 -> {
                categoryInfo = context.getString(R.string.obese_info)
                category = context.getString(R.string.obese)
            }
            else -> {
                categoryInfo = context.getString(R.string.severely_obese_info)
                category = context.getString(R.string.severely_obese)
            }

        }
        categoryDetails[context.getString(R.string.categoryInfo)] = categoryInfo
        categoryDetails[context.getString(R.string.category)] = category

        return categoryDetails
    }
}