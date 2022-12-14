package com.anorlddroid.patricemulindi.viewmodels

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anorlddroid.patricemulindi.model.Details
import com.anorlddroid.patricemulindi.model.Results
import com.anorlddroid.patricemulindi.repository.BMIRepository
import com.anorlddroid.patricemulindi.repository.FileRepository
import com.anorlddroid.patricemulindi.repository.ScreenshotRepository
import com.anorlddroid.patricemulindi.repository.usecase.CalculateBMIUseCase
import com.anorlddroid.patricemulindi.repository.usecase.OpenPlayMarketUseCase
import com.anorlddroid.patricemulindi.repository.usecase.ShareScreenshotUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mBMIRepo: BMIRepository,
    private val mBMIUseCase: CalculateBMIUseCase,
    private val mOpenPlayMarketUseCase: OpenPlayMarketUseCase,
    private val mFileRepo: FileRepository,
    private val mScreenshotRepo: ScreenshotRepository,
    private val mShareScreenshotUseCase: ShareScreenshotUseCase
) : ViewModel() {

    private val _results = MutableStateFlow(Results("", emptyList(), "", "", ""))
    val results: StateFlow<Results>
        get() = _results


    fun calculate(details: Details, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _results.value = mBMIUseCase.execute(details, context )
        }
    }

    fun shareScreenShot(activity: Activity, view: View) {
        val bitmap = mScreenshotRepo.takeScreenshot(view)
        val imageFile = mFileRepo.saveImageFile(activity, bitmap)
        mShareScreenshotUseCase.execute(activity, imageFile)
    }


    fun openPlayMarket(activity: Activity) {
        viewModelScope.launch {
            mOpenPlayMarketUseCase.execute(activity)
        }
    }


    fun convertToDetails(
        name: String,
        weight: Double,
        height: Double,
        gender: String
    ) = mBMIRepo.convertToDetails(
        name,
        weight,
        height,
        gender
    )

    fun verifyInput(name: String) = name.isNotEmpty()
}