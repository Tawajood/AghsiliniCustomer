package com.dotjoo.aghsilinicustomer.ui.fragment.main.home

import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.viewpager2.widget.ViewPager2
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.data.PrefsHelper
import com.dotjoo.aghsilinicustomer.data.remote.response.Address
import com.dotjoo.aghsilinicustomer.data.remote.response.Laundry
import com.dotjoo.aghsilinicustomer.data.remote.response.SliderHome
import com.dotjoo.aghsilinicustomer.databinding.FragmentHomeBinding
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.adapter.SectionsHomePagerAdapter
import com.dotjoo.aghsilinicustomer.ui.adapter.home.AllLaundriesPagingAdapter
import com.dotjoo.aghsilinicustomer.ui.adapter.home.Cardsize.DevisionCard
import com.dotjoo.aghsilinicustomer.ui.adapter.home.NearLaundriesAdapter
import com.dotjoo.aghsilinicustomer.ui.lisener.LaundryClickListener
import com.dotjoo.aghsilinicustomer.util.Constants
import com.dotjoo.aghsilinicustomer.util.PermissionManager
import com.dotjoo.aghsilinicustomer.util.WWLocationManager
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.ext.init
import com.dotjoo.aghsilinicustomer.util.ext.isNull
import com.dotjoo.aghsilinicustomer.util.observe
import com.dotjoo.aghsilinicustomer.util.openLocationSettingsResultLauncher
import com.dotjoo.aghsilinicustomer.util.requestAppPermissions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), LaundryClickListener {

    lateinit var adapterNearLaundries: NearLaundriesAdapter
    lateinit var adapterTopLaundries: AllLaundriesPagingAdapter
    val mViewModel: HomeViewModel by activityViewModels()
    private lateinit var handler: Handler
    private var urls: MutableList<String> = mutableListOf()
    var lat: Double? = null
    var lang: Double? = null

    @Inject
    lateinit var permissionManager: PermissionManager
    @Inject
    lateinit var locationManager: WWLocationManager

    private val runnable = Runnable {
        if (binding.viewpager.currentItem < (urls.size - 1)) {
            binding.viewpager.currentItem = binding.viewpager.currentItem + 1

        } else {
            binding.viewpager.currentItem = 0

        }
    }

    override fun onFragmentReady() {
        binding.tvAllLaundries.setPaintFlags(binding.tvAllLaundries.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding.tvAllNear.setPaintFlags(binding.tvAllNear.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        initAdapters()
        onClick()
    setAddress()

        mViewModel.apply {
           getSlider()
            observe(viewState) {
                handleViewState(it)
            }
        }
        binding.swiperefreshHome.setOnRefreshListener {
            setAddress()
            mViewModel.getSlider()
            if (binding.swiperefreshHome != null) binding.swiperefreshHome.isRefreshing = false
        }
    }

    private fun setAddress() {
     //   if (parent.lat == null) {
            if (PrefsHelper.getUserData().isNull() || PrefsHelper.getUserData() == null) {
                checkLocation()
            } else {

                mViewModel.getAllAddresses()
            }

     /*   } else {

            mViewModel.getAllLaundries(parent.lat.toString(), parent.long.toString())
            mViewModel.getNearestLaundries(parent.lat.toString(), parent.long.toString())
            binding.tvAddress.setText(parent.address)
        }*/
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

                }else if (it.contains("aghsilini.com") == true) {
                    showToast(resources.getString(R.string.connection_error))
                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is HomeAction.AllLaundries -> {
                lifecycleScope.launchWhenCreated {
                    adapterTopLaundries.submitData(action.data)
                }
            }

            is HomeAction.ShowCurrent -> {
                if(action.data ==null ||action.data.isNullOrEmpty()){
                    checkLocation ()
                }else {
                         var current : Address? = null
                      action. data?.forEach {
                            if(it.current==1) {
                                current = it
                            }
                        }
                    if(current==null)checkLocation()
                    else {
                        parent.lat = current?.lat?.toDoubleOrNull()
                        parent.long = current?.lon?.toDoubleOrNull()
                        parent.address = current?.address
                        binding?.tvAddress?.text = parent.address
                        mViewModel.getAllLaundries(parent.lat.toString(), parent.long.toString())
                        mViewModel.getNearestLaundries(
                            parent.lat.toString(),
                            parent.long.toString()
                        )
                    }
                }
            }

            is HomeAction.ShowNearestLaundries -> {
                action.data.laundries?.let { loadLaundries(it) }
            }

            is HomeAction.ShowSliderHome -> {
                action.data.sliders?.let { loadSlider(it) }
            }

            else -> {

            }
        }
    }


    lateinit var parent: MainActivity
    private fun onClick() {
        binding.tvAllLaundries.setPaintFlags(binding.tvAllLaundries.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding.tvAllNear.setPaintFlags(binding.tvAllNear.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        parent = requireActivity() as MainActivity
        parent.showBottomNav(true)
        binding.etSearch.setText("")
         binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_GO ||
                actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND) {
                findNavController().navigate(
                    R.id.allLaundriesFragment,
                    bundleOf(
                        Constants.All to Constants.SEARCH,
                        Constants.SEARCH to v.text.toString()
                    )
                )
                true
            }
            else {
                false
            }
        }

        binding.tvAllLaundries.setOnClickListener {
            findNavController().navigate(
                R.id.allLaundriesFragment, bundleOf(Constants.All to Constants.ALL_ALUNDRY)
            )
        }
        binding.tvAllNear.setOnClickListener {
            findNavController().navigate(
                R.id.allLaundriesFragment, bundleOf((Constants.All to Constants.NEAR_LAUNDRY))
            )

        }
    }

    private fun initAdapters() {
        adapterNearLaundries = NearLaundriesAdapter(this, DevisionCard)
        binding.rvLaundies.init(requireContext(), adapterNearLaundries, 1)


        //     loadLaundries(list)
        adapterTopLaundries = AllLaundriesPagingAdapter(this)
        binding.rvToplaundies.init(requireContext(), adapterTopLaundries, 2)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapterTopLaundries.loadStateFlow.collect {
                    binding.preProg.isVisible = it.source.prepend is LoadState.Loading
                    binding.appendProgress.isVisible = it.source.append is LoadState.Loading
                }
            }
        }
        adapterTopLaundries.addLoadStateListener { loadState ->

            // show empty list
            if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {
            }
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
                showProgress(false)
                if (adapterTopLaundries.itemCount < 1) {
                     binding.lytTopRatedlaundry.visibility = View.GONE

                } else {
                    binding.lytTopRatedlaundry.visibility = View.VISIBLE
                }
            } else {
// If we have an error, show a toast*/
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    /* if (it.error.message.equals(Constants.UNAUTHURAIZED_ACCESS)) {
                         showEmptyState(true)
                     } else*/
                    Toast.makeText(activity, it.error.message.toString(), Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    private fun loadLaundries(list: ArrayList<Laundry>) {
        //  Handler(Looper.getMainLooper()).postDelayed({
        if (list.size > 0) {

            adapterNearLaundries.ordersList = list
            adapterNearLaundries.notifyDataSetChanged()
            binding.lytNearestLaundry.isVisible = true
        }
    }

    private fun loadSlider(mainSliders: ArrayList<SliderHome>) {
        handler = Handler(Looper.myLooper()!!)
        urls.clear()
        if (mainSliders != null) {
            for (x in mainSliders) {
                urls.add(x.img.toString())
            }
            val adapter = activity?.let { SectionsHomePagerAdapter(it, binding.viewpager, urls) }
            binding.viewpager.adapter = adapter
            binding.viewpager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    handler.removeCallbacks(runnable)
                    handler.postDelayed(runnable, 4000)
                }
            })
        }
    }

    private fun checkLocation() {
        if (permissionManager.hasAllLocationPermissions()) {
            checkIfLocationEnabled()
        } else {
            permissionsLauncher?.launch(permissionManager.getAllLocationPermissions())
        }
    }


    private val permissionsLauncher = requestAppPermissions { allIsGranted, _ ->
        if (allIsGranted) {
            checkIfLocationEnabled()
        } else {
            Toast.makeText(
                activity, getString(R.string.not_all_permissions_accepted), Toast.LENGTH_LONG
            ).show()
        }
    }


    private fun checkIfLocationEnabled() {
        /////////////////////////////////////////  request()
        if (locationManager.isLocationEnabled()) {

            request()


        } else {
            Log.d("location", "NoisLocationEnabled")
            activity?.let {
                locationManager.showAlertDialogButtonClicked(
                    it, locationSettingLauncher
                )
            }
        }
    }

    private fun request() {
        locationManager.getLastKnownLocation { location ->
            //  binding.lytData.isVisible = true
            lat = location.latitude
            lang = location.longitude
            lat?.let {
                var address = locationManager.getAddress(it, lang)
                parent.lat = it
                parent.long = lang
                parent.address = address?.street + " , " + address?.city
                binding?.tvAddress?.text = parent.address

                mViewModel.getAllLaundries(it.toString(), lang.toString())
                mViewModel.getNearestLaundries(it.toString(), lang.toString())
            }
        }


    }

    private val locationSettingLauncher = openLocationSettingsResultLauncher {
        checkIfLocationEnabled()
    }

    override fun onPause() {
        super.onPause()
        try {
            handler.removeCallbacks(runnable)

        } catch (e: Exception) {

        }
    }

    override fun onResume() {
        super.onResume()
        try {
            handler.postDelayed(runnable, 4000)

        } catch (e: Exception) {

        }
    }

    override fun onInfoClickLisener(item: Laundry) {
        findNavController().navigate(R.id.laundryInfoFragment, bundleOf(Constants.Laundry to item))
    }

    override fun onRateClickLisener(item: Laundry) {
        findNavController().navigate(R.id.laundrRateFragment, bundleOf(Constants.Laundry to item))
    }
}