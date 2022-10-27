package com.anorlddroid.patricemulindi.viewmodels

import android.app.Activity
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anorlddroid.patricemulindi.model.Details
import com.anorlddroid.patricemulindi.model.Results
import com.anorlddroid.patricemulindi.repository.BMIRepository
import com.anorlddroid.patricemulindi.repository.usecase.CalculateBMIUseCase
import com.anorlddroid.patricemulindi.repository.usecase.CalculateBMIUseCaseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mBMIRepo: BMIRepository,
    private val mBMIUseCase: CalculateBMIUseCase
) : ViewModel() {

    private val _results = MutableStateFlow(Results("", emptyList(), "", "", ""))
    val results: StateFlow<Results>
        get() = _results

    private val _bmiResultWholeNum = MutableStateFlow("")
    val mBMIResultWholeNum: StateFlow<String>
        get() = _bmiResultWholeNum

    private val _bmiResultRemNum = MutableStateFlow("")
    val mBMIResultRemNum: StateFlow<String>
        get() = _bmiResultRemNum

    private val _rate = MutableStateFlow("")
    val mRate: StateFlow<String>
        get() = _rate

    private val _bmiInfo = MutableStateFlow("")
    val mBMIInfo: StateFlow<String>
        get() = _bmiInfo

    private val _ponderalIndex = MutableStateFlow("")
    val mPonderalInde: StateFlow<String>
        get() = _ponderalIndex


    fun calculate(details: Details) {
        viewModelScope.launch(Dispatchers.IO) {
            _results.value = mBMIUseCase.execute(details)
        }
    }




//    fun shareScreenShot(activity: Activity, view: View) = mBMIRepo.shareScreenShot(activity, view)
//
//
//    fun openPlayMarket(activity: Activity) {
//        viewModelScope.launch {
//            mBMIRepo.openPlayMarket(activity)
//        }
//    }


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