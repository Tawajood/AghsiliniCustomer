package com.dotjoo.aghsilinicustomer.ui.fragment.main.laundryInfo

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.data.remote.response.Laundry
import com.dotjoo.aghsilinicustomer.data.remote.response.ReviewItem
import com.dotjoo.aghsilinicustomer.databinding.FragmentLaundrRateBinding
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.adapter.laundry_info.RateLaundriesAdapter
import com.dotjoo.aghsilinicustomer.ui.fragment.main.home.HomeAction
import com.dotjoo.aghsilinicustomer.ui.fragment.main.home.HomeViewModel
import com.dotjoo.aghsilinicustomer.util.Constants
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.ext.init
import com.dotjoo.aghsilinicustomer.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaundrRateFragment : BaseFragment<FragmentLaundrRateBinding>() {
    val mViewModel : HomeViewModel by activityViewModels ()
    lateinit var adapter : RateLaundriesAdapter
    var launary :Laundry? =null
    override fun onFragmentReady() {
        initAdapters()
onClick()
         launary = arguments?.getParcelable(Constants.Laundry)
        binding.tvRates.setText(launary?.rate)
        mViewModel.apply {
            launary?.id?.let { getLaundryReview(it) }
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            launary?.id?.let { mViewModel.getLaundryReview(it) }
             if (binding.swiperefreshHome != null) binding.swiperefreshHome.isRefreshing = false
        }
    }

    fun handleViewState(action: HomeAction) {
        when (action) {
            is HomeAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }


            is HomeAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)

                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is HomeAction.ShowLaundryReviews -> {
             action.data.reviews?.let {


                if (it.size > 0) {
                    loadReviewItem(it)

                    binding.rvRates.isVisible= true
                    binding.lytEmptyState.isVisible = false
                } else {
                    binding.rvRates.isVisible= false
                    binding.lytEmptyState.isVisible = true
                }
            }
            }

            else -> {

            }
        }
    }


    lateinit var parent: MainActivity
    private fun onClick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)

        binding.cardBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    fun initAdapters() {
        adapter = RateLaundriesAdapter( )
        binding.rvRates.init(requireContext(), adapter, 2)


    }

    private fun loadReviewItem(list: ArrayList<ReviewItem>) {

             adapter.ordersList = list
             adapter.notifyDataSetChanged()

    }



}