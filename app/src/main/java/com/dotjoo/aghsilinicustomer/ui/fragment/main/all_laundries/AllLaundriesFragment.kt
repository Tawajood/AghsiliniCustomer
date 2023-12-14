package com.dotjoo.aghsilinicustomer.ui.fragment.main.all_laundries

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.data.remote.response.Laundry
import com.dotjoo.aghsilinicustomer.databinding.FragmentAllLaundriesBinding
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.adapter.home.AllLaundriesPagingAdapter
import com.dotjoo.aghsilinicustomer.ui.adapter.home.Cardsize
import com.dotjoo.aghsilinicustomer.ui.adapter.home.NearLaundriesAdapter
import com.dotjoo.aghsilinicustomer.ui.fragment.main.home.HomeAction
import com.dotjoo.aghsilinicustomer.ui.fragment.main.home.HomeViewModel
import com.dotjoo.aghsilinicustomer.ui.lisener.LaundryClickListener
import com.dotjoo.aghsilinicustomer.util.Constants
import com.dotjoo.aghsilinicustomer.util.PermissionManager
import com.dotjoo.aghsilinicustomer.util.WWLocationManager
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.ext.init
import com.dotjoo.aghsilinicustomer.util.observe
import com.dotjoo.aghsilinicustomer.util.openLocationSettingsResultLauncher
import com.dotjoo.aghsilinicustomer.util.requestAppPermissions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint

class AllLaundriesFragment : BaseFragment<FragmentAllLaundriesBinding>(), LaundryClickListener {

    private var state: String? = null
    var adapterNearLaundries: NearLaundriesAdapter? = null
    var adapterTopLaundries: AllLaundriesPagingAdapter? = null

    val mViewModel: HomeViewModel by activityViewModels()

    @Inject
    lateinit var permissionManager: PermissionManager

    @Inject
    lateinit var locationManager: WWLocationManager
    var lat: String? = null
    var lang: String? = null
    override fun onFragmentReady() {
        state = arguments?.getString(Constants.All)

        onclick()
        mViewModel.apply {
            checkLocation()
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            checkLocation()
            //mViewModel.getAllLaundries(lat,lang)
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

            is HomeAction.AllLaundries -> {
                lifecycleScope.launchWhenCreated {
                    adapterTopLaundries?.submitData(action.data)
                }
            }
  is HomeAction.ShowSearchResult -> {
                lifecycleScope.launchWhenCreated {
                    adapterTopLaundries?.submitData(action.data)
                }
            }

            is HomeAction.ShowNearestLaundries -> {
                action.data.laundries?.let { loadLaundries(it) }
            }

            else -> {

            }
        }
    }

    private fun loadLaundries(list: ArrayList<Laundry>) {
        adapterNearLaundries?.ordersList = list
        adapterNearLaundries?.notifyDataSetChanged()
        binding.lytData.isVisible = true
    }

    lateinit var parent: MainActivity
    private fun onclick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)
        binding.icArrow.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.icClearTxt.setOnClickListener {
        if(!binding.etSearch.text.isNullOrEmpty()) {
            binding.etSearch.setText("")
            checkLocation()
        }
        }
        if (state == Constants.ALL_ALUNDRY) {
            initAdaptersAll()
            binding.tvNearestLaundry.setText(resources.getString(R.string.top_rated_laundry))
            binding.etSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    mViewModel.searchLaundry(s.toString())
                }

                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        } else {
            initAdaptersNear()
            binding.tvNearestLaundry.setText(resources.getString(R.string.nearest_laundries))
            binding.etSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    adapterNearLaundries?.filter?.filter(s)
                }

                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
    }

    private fun initAdaptersNear() {
        adapterNearLaundries = NearLaundriesAdapter(this, Cardsize.FullCard)
        binding.rvLaundies.init(requireContext(), adapterNearLaundries, 2)


    }

    private fun initAdaptersAll() {
        adapterNearLaundries = NearLaundriesAdapter(this, Cardsize.FullCard)
        binding.rvLaundies.init(requireContext(), adapterNearLaundries, 2)


        //     loadLaundries(list)
        adapterTopLaundries = AllLaundriesPagingAdapter(this)
        binding.rvLaundies.init(requireContext(), adapterTopLaundries, 2)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapterTopLaundries?.loadStateFlow?.collect {
                    binding.preProg.isVisible = it.source.prepend is LoadState.Loading
                    binding.appendProgress.isVisible = it.source.append is LoadState.Loading
                }
            }
        }
        adapterTopLaundries?.addLoadStateListener { loadState ->

            // show empty list
            if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {
            }
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
                showProgress(false)
                if (adapterTopLaundries?.itemCount!! < 1) {
                    binding.lytEmptyState.visibility = View.VISIBLE
                    binding.lytData.visibility = View.GONE

                } else {
                    binding.lytData.visibility = View.VISIBLE
                    binding.lytEmptyState.visibility = View.GONE
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
            lat = location.latitude.toString()
            lang = location.longitude.toString()
            lat?.let {
                if (state == Constants.ALL_ALUNDRY) {

                     mViewModel.getAllLaundries(it, lang!!)

                } else {

                     mViewModel.getNearestLaundries(it, lang!!)
                }
            }
        }


    }

    private val locationSettingLauncher = openLocationSettingsResultLauncher {
        checkIfLocationEnabled()
    }


    override fun onInfoClickLisener(item: Laundry) {
        findNavController().navigate(R.id.laundryInfoFragment, bundleOf(Constants.Laundry to item))
    }

    override fun onRateClickLisener(item: Laundry) {
        findNavController().navigate(R.id.laundrRateFragment, bundleOf(Constants.Laundry to item))
    }


}