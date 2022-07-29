package com.anorlddroid.patricemulindi.usecases

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.anorlddroid.patricemulindi.BuildConfig
import com.anorlddroid.patricemulindi.model.Details
import com.anorlddroid.patricemulindi.model.Results
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject

interface BMIRepository {
    fun calculateBMIAndPonderalIndex(personalBMIDetails: Details) : Results
    fun shareScreenShot(activity: Activity, view: View)
    fun openPlayMarket(activity: Activity)
}


class BMIRepositoryImpl  @Inject constructor(): BMIRepository{
    override fun calculateBMIAndPonderalIndex(personalBMIDetails: Details): Results {
        val bmi: Double = personalBMIDetails.weight / ((personalBMIDetails.height * personalBMIDetails.height) / 10000)
        val ponderal : Double = personalBMIDetails.weight / ((personalBMIDetails.height * personalBMIDetails.height * personalBMIDetails.height) / 1000000)
        val category: String
        val categoryInfo: String
        if (bmi < 18.5) {
            categoryInfo = "Underweight BMI range: Less than 18.5kg/m2"
            category = "Underweight"
        } else if (bmi in 18.5..24.9) {
            categoryInfo = "Normal BMI range: 18.5kg/m2 - 24.9kg/m2"
            category = "Normal"
        }else if (bmi in 25.0..29.9) {
            categoryInfo =  "Overweight BMI range: 25kg/m2 - 29.9kg/m2"
            category = "Overweight"
        }else if (bmi in 30.0..39.9) {
            categoryInfo = "Obese BMI range: 30kg/m2 - 39.9kg/m2"
            category = "Obese"

        }else {
            categoryInfo = "Severely Obese BMI range: Greater than 40kg/m2"
            category = "Severely Obese"
        }
        val bmIndex = String.format("%.2f", bmi)

        return Results(
            name = personalBMIDetails.name,
            BMIIndex = bmIndex.split("."),
            PonderalIndex = String.format("Ponderal Index Range: %.2fkg/m3 ", ponderal),
            category = category,
            categoryInfo = categoryInfo
        )
    }

    override fun shareScreenShot(activity: Activity, view: View) {
        val date = Date()
        android.text.format.DateFormat.format("yyyy/MM/dd-hh:mm:ss", date)
        val name = date.toString().replace("\\s".toRegex(), "")
        try {
            verifyPermission(activity)
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            val imageFile = File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$name.jpg")
            val outputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            val uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + "."+activity.localClassName+".provider", imageFile)

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/jpeg"
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(activity, Intent.createChooser(shareIntent, "Share Image"), null)

        }catch (ex: Exception){
            if (BuildConfig.DEBUG){
                Log.e("Screenshot", "Exception: $ex ")
            }
        }
    }

    override fun openPlayMarket(activity: Activity) {
        val intent = activity.packageManager.getLaunchIntentForPackage("com.android.vending")
        if (intent != null) {
            startActivity(activity, intent, null)
        }
    }

    private fun verifyPermission(activity: Activity){
        val permissionStorage : Array<String> = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissions = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissions != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, permissionStorage, 1)
        }
    }


}