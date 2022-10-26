package com.anorlddroid.patricemulindi.views

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.anorlddroid.patricemulindi.BuildConfig
import com.anorlddroid.patricemulindi.R
import com.anorlddroid.patricemulindi.databinding.FragmentResultsBinding
import com.anorlddroid.patricemulindi.domain.MainActivityObserver
import com.anorlddroid.patricemulindi.viewmodels.MainViewModel
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultsFragment : Fragment() {


    private lateinit var binding: FragmentResultsBinding
    private val args: ResultsFragmentArgs by navArgs()
    private val viewModel: MainViewModel by viewModels()
    var currentNativeAd: NativeAd? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultsBinding.inflate(inflater, container, false)

        binding.BMIResultWholeNum.text = args.results.BMIIndex[0]
        binding.BMIResultRemNum.text = args.results.BMIIndex[1]
        binding.txtVwNameRate.text =
            "HELLO ${args.results.name.uppercase()}, YOU ARE ${args.results.category.uppercase()}"
        binding.txtVwBMIInfo.text = args.results.categoryInfo
        binding.txtVwPonderalIndex.text = args.results.PonderalIndex
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MobileAds.initialize(requireContext())
        val adBuilder = AdLoader.Builder(requireContext(), getString(R.string.native_unit_id))
        adBuilder.forNativeAd { nativeAd ->
            // If this callback occurs after the activity is destroyed, you must call
            // destroy and return or you may get a memory leak.
            var activityDestroyed = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                activityDestroyed = activity?.isDestroyed == true
            }
            if (activityDestroyed || activity?.isFinishing == true || activity?.isChangingConfigurations == true) {
                nativeAd.destroy()
                return@forNativeAd
            }
            // Destroy all old ads first
            currentNativeAd?.destroy()
            currentNativeAd = nativeAd
            val adView = layoutInflater
                .inflate(R.layout.nativead, null) as NativeAdView
            populateNativeAdView(nativeAd, adView)
            binding.nativeAd.removeAllViews()
            binding.nativeAd.elevation = 12F
            binding.nativeAd.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.frame_layout_bg)
            binding.nativeAd.addView(adView)
        }
        val adLoader = adBuilder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                val error =
                    """
           domain: ${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}
          """"
                if (BuildConfig.DEBUG) {
                    Log.d(
                        "ADLOADER", "Failed to load native ad with error $error"
                    )
                }
            }
        }).build()
        adLoader.loadAd(AdRequest.Builder().build())
        activity?.lifecycle?.addObserver(MainActivityObserver {
            val toolbar: Toolbar = view.findViewById(R.id.tool_bar)
            (activity as AppCompatActivity).setSupportActionBar(
                toolbar
            )
            toolbar.setNavigationOnClickListener {
                findNavController().navigate(R.id.action_navigation_results_to_navigation_home)
            }
        })



        binding.shareBtn.setOnClickListener {
            activity?.let { it1 -> viewModel.shareScreenShot(it1, binding.root) }
        }
        binding.rateBtn.setOnClickListener {
            activity?.let { it1 -> viewModel.openPlayMarket(it1) }
        }
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        adView.mediaView = adView.findViewById(R.id.ad_media)
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        // The headline and media content are guaranteed to be in every NativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline
        nativeAd.mediaContent?.let { adView.mediaView?.setMediaContent(it) }

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView?.visibility = View.INVISIBLE
        } else {
            adView.bodyView?.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.callToAction == null) {
            adView.callToActionView?.visibility = View.INVISIBLE
        } else {
            adView.callToActionView?.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.icon == null) {
            adView.iconView?.visibility = View.GONE
        } else {
            adView.iconView?.visibility = View.VISIBLE
            (adView.iconView as? ImageView)?.setImageDrawable(
                nativeAd.icon?.drawable
            )

        }

        if (nativeAd.price == null) {
            adView.priceView?.visibility = View.INVISIBLE
        } else {
            adView.priceView?.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }

        if (nativeAd.store == null) {
            adView.storeView?.visibility = View.INVISIBLE
        } else {
            adView.storeView?.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }

        if (nativeAd.starRating == null) {
            adView.starRatingView?.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView?.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null) {
            adView.advertiserView?.visibility = View.INVISIBLE
        } else {
            adView.advertiserView?.visibility = View.VISIBLE
            (adView.advertiserView as TextView).text = nativeAd.advertiser

        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)
    }


    override fun onDestroy() {
        currentNativeAd?.destroy()
        super.onDestroy()
    }

}