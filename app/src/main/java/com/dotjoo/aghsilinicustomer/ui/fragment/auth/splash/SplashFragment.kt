package com.dotjoo.aghsilinicustomer.ui.fragment.auth.splash

import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.data.PrefsHelper
import com.dotjoo.aghsilinicustomer.databinding.FragmentSplashBinding
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.util.ext.showActivity
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay

class SplashFragment : BaseFragment<FragmentSplashBinding>() {


    override fun onFragmentReady() {
        lifecycleScope.launchWhenResumed {
            delay(1500)
            //  Handler(Looper.getMainLooper()).postDelayed({

            if (PrefsHelper.getLoggedinBefore()== false) {
                PrefsHelper.setLoggedInBefore(true)
                findNavController().navigate(
                    R.id.walkThroughFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
                )
            } else {
                showActivity(MainActivity::class.java, clearAllStack = true)
            }
        }
    }




    override fun onPause() {
        lifecycleScope.cancel()
         super.onPause()
    }

}
