package com.anorlddroid.patricemulindi.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.anorlddroid.patricemulindi.R
import com.anorlddroid.patricemulindi.databinding.FragmentHomeBinding
import com.anorlddroid.patricemulindi.model.Details
import com.anorlddroid.patricemulindi.viewmodels.MainViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MainViewModel>()
    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var adRequest: AdRequest
    private var weight by Delegates.notNull<Int>()
    private var height by Delegates.notNull<Int>()
    private lateinit var gender: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: Toolbar = view.findViewById(R.id.tool_bar);
        (activity as AppCompatActivity).setSupportActionBar(
            toolbar
        )
        loadInterstitialAd()
        populateNumberPickers()

        binding.calculateBtn.setOnClickListener {
            val name = binding.name.text.toString()
            if (viewModel.verifyInput(name)) {
                val details = viewModel.convertToDetails(
                    name = name,
                    weight = weight.toDouble(),
                    height = height.toDouble(),
                    gender = gender
                )
                if (mInterstitialAd != null) {
                    showInterstitialAd(details = details)
                } else {
                    navigateToResultFragment(details = details)
                }
            } else {
                Toast.makeText(context, getString(R.string.invalid_input), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun loadInterstitialAd() {
        adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            requireContext(),
            getString(R.string.interstitial_unit_id),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(insterstitialAd: InterstitialAd) {
                    mInterstitialAd = insterstitialAd
                }
            })
    }

    private fun populateNumberPickers() {
        val weightPicker: NumberPicker = binding.numberPickerWeight
        weightPicker.maxValue = 300
        weightPicker.minValue = 10
        weightPicker.value = 43
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
        val genderList = arrayOf(getString(R.string.male), getString(R.string.female))
        genderPicker.maxValue = 1
        genderPicker.minValue = 0
        genderPicker.value = 1
        gender = genderList[1]
        genderPicker.displayedValues = genderList
        genderPicker.setOnValueChangedListener { _, _, newValue ->
            gender = genderList[newValue]
        }
    }

    private fun showInterstitialAd(details: Details) {
        mInterstitialAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    navigateToResultFragment(details = details)
                    mInterstitialAd = null
                }
            }
        mInterstitialAd?.show(requireActivity())
    }

    private fun navigateToResultFragment(details: Details) {
        val actions =
            HomeFragmentDirections.actionNavigationHomeToNavigationResults(details)
        findNavController().navigate(actions)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}