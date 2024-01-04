package com.dotjoo.aghsilinicustomer.ui.fragment.auth.walkThrough

import android.content.Intent
import android.graphics.Color
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.data.PrefsHelper
import com.dotjoo.aghsilinicustomer.databinding.FragmentWalkThroughBinding
import com.dotjoo.aghsilinicustomer.ui.activity.AuthActivity
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.util.Constants
import com.dotjoo.aghsilinicustomer.util.ext.showActivity
import com.google.android.material.tabs.TabLayoutMediator

class WalkThroughFragment :  BaseFragment<FragmentWalkThroughBinding>() {

    private var pos = 0
    override fun onFragmentReady() {
        setupViewPager()
        onClick()

    }


    private fun setupViewPager() {


        var circularStatusView = binding.circularStatusView
        circularStatusView.setPortionsCount(2);
        circularStatusView.setPortionsColor(getResources().getColor(R.color.blue) )
             //set all portions color
      //   circularStatusView.setPortionSpacing(0);//set the spacing between portions
    //    circularStatusView.setPortionWidth(0F);//set portion width
        var  blueColorValue = Color.parseColor("#25AAE2")
        var  lightgreyColorValue = Color.parseColor("#F0F0F0")
        circularStatusView.setPortionColorForIndex(1, lightgreyColorValue)
                circularStatusView.setPortionColorForIndex(0, blueColorValue)




          val adapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ ->
            binding.viewPager.currentItem = 0
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                super.onPageSelected(position)
                pos = position

                when (pos) {
                    0 -> {

binding.ivNext.isVisible= true
                        binding.tvStart.isVisible= false
                        circularStatusView.setPortionColorForIndex(1, lightgreyColorValue)
                        circularStatusView.setPortionColorForIndex(0, blueColorValue)

                    }

                    1 -> {
                        binding.ivNext.isVisible= false
                        binding.tvStart.isVisible= true
                        circularStatusView.setPortionColorForIndex(1, blueColorValue)
                        circularStatusView.setPortionColorForIndex(0, blueColorValue)
                    }


                }
            }
        })
    }

    private fun onClick() {

        if (PrefsHelper.getLanguage() == Constants.AR) {
            binding.tvLang.text = "EN"
        } else {
            binding.tvLang.text = "العربية"
        }

        binding.tvLang.setOnClickListener {
            if (PrefsHelper.getLanguage() == Constants.EN) {
                PrefsHelper.setLanguage(Constants.AR)
                var intent = Intent(activity, AuthActivity::class.java)
                intent.putExtra(Constants.Start, Constants.WALK_THROUGHT)
                activity?. startActivity(intent)
                activity?.finish()

            } else {
                PrefsHelper.setLanguage(Constants.EN)
                var intent = Intent(activity, AuthActivity::class.java)
                intent.putExtra(Constants.Start, Constants.WALK_THROUGHT)
                activity?. startActivity(intent)
                activity?.finish()
             }
        }
        binding.lytNext.setOnClickListener {
            if (pos == 0  )
                goNext()
            else
                gotoLMain()
        }
binding.tvSkip.setOnClickListener {
    gotoLMain()

}


    }


    private fun goNext() {
        if (pos == 0  ) {
            pos++
            binding.viewPager.currentItem = binding.viewPager.currentItem + 1
        }
    }

    private fun gotoLMain() {

        // findNavController().navigate(R.id.loginFragment)
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }
}

