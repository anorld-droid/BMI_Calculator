package com.anorlddroid.patricemulindi.views

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

import com.anorlddroid.patricemulindi.R
import com.anorlddroid.patricemulindi.model.Details
import com.anorlddroid.patricemulindi.databinding.FragmentHomeBinding
import com.anorlddroid.patricemulindi.domain.MainActivityObserver
import com.anorlddroid.patricemulindi.viewmodels.MainViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding
    private  val viewModel by viewModels<MainViewModel>()
    private  var mInterstitialAd: InterstitialAd? = null
    private lateinit var adRequest: AdRequest
    private var weight by Delegates.notNull<Int>()
    private var height by Delegates.notNull<Int>()
    private lateinit var gender: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.lifecycle?.addObserver(MainActivityObserver{
            val toolbar: Toolbar = binding.homeAppBar
            (activity as AppCompatActivity).setSupportActionBar(
                toolbar
            )
            val weightPicker: NumberPicker = binding.numberPickerWeight
            weightPicker.maxValue = 300
            weightPicker.minValue = 10
            weightPicker.value  = 43
            weight = 43
            weightPicker.setOnValueChangedListener { _, _, newValue ->
                weight = newValue
            }

            val heightPicker: NumberPicker = binding.numberPickerHeight
            heightPicker.maxValue = 300
            heightPicker.minValue = 10
            heightPicker.value = 127
            height = 127
            heightPicker.setOnValueChangedListener { _, _, newValue ->
                height = newValue
            }

            val genderPicker: NumberPicker = binding.numberPickerGender
            val genderList = arrayOf("Male", "Female")
            genderPicker.maxValue = 1
            genderPicker.minValue = 0
            genderPicker.value = 1
            gender = genderList[1]
            genderPicker.displayedValues = genderList
            genderPicker.setOnValueChangedListener { _, _, newValue ->
                gender = genderList[newValue]
            }
        })

        MobileAds.initialize(requireContext())
        adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(), getString(R.string.interstitial_unit_id), adRequest, object: InterstitialAdLoadCallback(){
            override fun onAdLoaded(insterstitialAd: InterstitialAd){
                mInterstitialAd = insterstitialAd
            }
        })

        binding.calculateBtn.setOnClickListener {
            if (verifyInputs()) {
                val result = viewModel.calculate(
                    Details(
                        name = binding.name.text.toString(),
                        weight = weight.toDouble(),
                        height = height.toDouble(),
                        gender = gender
                    )
                )
                if (mInterstitialAd != null){
                    mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback(){
                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                            val actions = HomeFragmentDirections.actionNavigationHomeToNavigationResults(result)
                            findNavController().navigate(actions)
                            mInterstitialAd = null
                        }
                    }
                    mInterstitialAd?.show(requireActivity())
                }else {
                    val actions =
                        HomeFragmentDirections.actionNavigationHomeToNavigationResults(result)
                    findNavController().navigate(actions)
                }
            }
        }
    }

    private fun verifyInputs() : Boolean{
        if (binding.name.text.toString().isEmpty() ) {
            Toast.makeText(activity, "Enter your personal details to continue", Toast.LENGTH_LONG).show()
            return false
        }
        return  true
    }


}