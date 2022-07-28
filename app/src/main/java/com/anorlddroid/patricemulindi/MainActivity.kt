package com.anorlddroid.patricemulindi

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.anorlddroid.patricemulindi.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarGradient(activity = this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun setStatusBarGradient(activity: Activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val window = activity.window
            val background = ContextCompat.getDrawable(activity, R.drawable.statusbar_gradient_bg_color)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(activity, android.R.color.transparent)
            window.navigationBarColor = ContextCompat.getColor(activity, android.R.color.transparent)
            window.setBackgroundDrawable(background)
        }
    }
}