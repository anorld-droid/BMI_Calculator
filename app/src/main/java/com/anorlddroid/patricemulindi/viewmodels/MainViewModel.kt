package com.anorlddroid.patricemulindi.viewmodels

import android.app.Activity
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anorlddroid.patricemulindi.model.Details
import com.anorlddroid.patricemulindi.model.Results
import com.anorlddroid.patricemulindi.usecases.BMIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: BMIRepository
): ViewModel() {
       private val _results = MutableStateFlow(Results("", emptyList(), "", "", ""))
        val results : StateFlow<Results>
            get() = _results


    fun calculate(details: Details) = repo.calculateBMIAndPonderalIndex(personalBMIDetails = details)



    fun shareScreenShot(activity: Activity, view: View) = repo.shareScreenShot(activity, view)


    fun openPlayMarket(activity: Activity){
        viewModelScope.launch {
            repo.openPlayMarket(activity)
        }
    }

}