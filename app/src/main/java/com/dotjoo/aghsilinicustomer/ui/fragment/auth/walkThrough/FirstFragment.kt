package com.dotjoo.aghsilinicustomer.ui.fragment.auth.walkThrough

import com.bumptech.glide.Glide
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.databinding.ItemWalkthrougthBinding


class FirstFragment(var state: Int) : BaseFragment<ItemWalkthrougthBinding>() {


    private fun setUpUi() {

        when (state) {
            0 -> {
                binding.tvTitleSlider.text = getString(R.string.walk_througt_title_first)
                binding.tvContentSlider.text =
                    getString(R.string.walk_througt_body_first)

                activity?.let {
                    Glide.with(it)
                        .load(R.drawable.i1)
                        .into(binding.imgSlider)
                }

            }
            1 -> {
                binding.tvTitleSlider.text = getString(R.string.walk_throught_title_sec)
                binding.tvContentSlider.text =
                    getString(R.string.walk_througt_body_sec)
                activity?.let {
                    Glide.with(it)
                        .load(R.drawable.i2)
                        .into(binding.imgSlider)
                }

            }


        }

    }

    override fun onFragmentReady() {
        setUpUi()
    }
}