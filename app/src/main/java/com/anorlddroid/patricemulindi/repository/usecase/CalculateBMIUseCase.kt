package com.anorlddroid.patricemulindi.repository.usecase

import com.anorlddroid.patricemulindi.model.Details
import com.anorlddroid.patricemulindi.model.Results
import com.anorlddroid.patricemulindi.repository.BMIRepository
import javax.inject.Inject

interface CalculateBMIUseCase {
    fun calculateBMI(weight: Double, height: Double): Double
    fun calculatePonderalIndex(weight: Double, height: Double): Double
    fun getCategory(mBMI: Double): Map<String, String>
    fun execute(userDetails: Details): Results

}

class CalculateBMIUseCaseImpl @Inject constructor(
    private val mBMIRepo: BMIRepository
) : CalculateBMIUseCase {
    override fun execute(userDetails: Details): Results {
        val bmi: Double = calculateBMI(userDetails.weight, userDetails.height)

        val ponderal: Double = calculatePonderalIndex(userDetails.weight, userDetails.height)
        val categoryDetails: Map<String, String> = getCategory(bmi)
        val category: String = categoryDetails["category"]!!

        val categoryInfo = categoryDetails["categoryInfo"]!!
        val bmIndex = String.format("%.2f", bmi)

        return mBMIRepo.convertToResults(
            name = userDetails.name,
            BMIIndex = bmIndex.split("."),
            PonderalIndex = String.format("Ponderal Index Range: %.2fkg/m3 ", ponderal),
            category = category,
            categoryInfo = categoryInfo
        )
    }

    override fun calculateBMI(weight: Double, height: Double) = weight / ((height * height) / 10000)

    override fun calculatePonderalIndex(weight: Double, height: Double) =
        weight / ((height * height * height) / 1000000)

    override fun getCategory(mBMI: Double): Map<String, String> {
        val category: String
        val categoryInfo: String
        val categoryDetails: MutableMap<String, String> = mutableMapOf()

        when (mBMI) {
            in 0.0..18.4 -> {
                categoryInfo = "Underweight BMI range: Less than 18.5kg/m2"
                category = "Underweight"
            }
            in 18.5..24.9 -> {
                categoryInfo = "Normal BMI range: 18.5kg/m2 - 24.9kg/m2"
                category = "Normal"
            }
            in 25.0..29.9 -> {
                categoryInfo = "Overweight BMI range: 25kg/m2 - 29.9kg/m2"
                category = "Overweight"
            }
            in 30.0..39.9 -> {
                categoryInfo = "Obese BMI range: 30kg/m2 - 39.9kg/m2"
                category = "Obese"
            }
            else -> {
                categoryInfo = "Severely Obese BMI range: Greater than 40kg/m2"
                category = "Severely Obese"
            }

        }
        categoryDetails["categoryInfo"] = categoryInfo
        categoryDetails["category"] = category

        return categoryDetails
    }
}