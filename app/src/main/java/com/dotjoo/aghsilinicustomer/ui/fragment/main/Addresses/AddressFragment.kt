package com.dotjoo.aghsilinicustomer.ui.fragment.main.Addresses

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.data.remote.response.Address
import com.dotjoo.aghsilinicustomer.databinding.FragmentAddressBinding
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.adapter.AllAddressAdapter
import com.dotjoo.aghsilinicustomer.ui.fragment.main.basket.CreateOrderAction
import com.dotjoo.aghsilinicustomer.ui.fragment.main.basket.CreateOrderViewModel
import com.dotjoo.aghsilinicustomer.ui.fragment.auth.register.MapBottomSheet
import com.dotjoo.aghsilinicustomer.ui.lisener.OnAddressAddesClickLisener
import com.dotjoo.aghsilinicustomer.ui.lisener.OnAllAddressClickLisener
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.ext.init
import com.dotjoo.aghsilinicustomer.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.card_back

@AndroidEntryPoint
class AddressFragment : BaseFragment<FragmentAddressBinding>(), OnAllAddressClickLisener {
    val mViewModel: CreateOrderViewModel by viewModels()
    var positionToDelete: Int? = null
    var positionToDefault: Int? = null
    lateinit var addapter: AllAddressAdapter
    lateinit var parent: MainActivity

    override fun onFragmentReady() {
        onclick()
        initAdapter()
        mViewModel.apply {
            getAllAddresses()
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getAllAddresses()
            if (binding.swiperefreshHome != null) binding.swiperefreshHome.isRefreshing = false
        }
    }

    fun handleViewState(action: CreateOrderAction) {
        when (action) {
            is CreateOrderAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }


            is CreateOrderAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)

                } else if (it.contains("aghsilini.com") == true) {
                    showToast(resources.getString(R.string.connection_error))
                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is CreateOrderAction.AllAddressShown -> {
                action.data.address?.let {
                    if (it.size > 0) {
                        addapter.addressList = it
                        addapter.notifyDataSetChanged()
                        binding.rvLaundies.isVisible = true
                        binding.lytEmptyState.isVisible = false
                    } else {
                        binding.rvLaundies.isVisible = false
                        binding.lytEmptyState.isVisible = true
                    }
                }
            }

            is CreateOrderAction.AddressDeleted -> {

                positionToDelete?.let { addapter.deleteItem(action.item, it) }
            }

            is CreateOrderAction.AddressDefault -> {

                mViewModel.getAllAddresses()
            }

            else -> {

            }
        }
    }

    private fun initAdapter() {
        addapter = AllAddressAdapter(this)
        binding.rvLaundies.init(requireContext(), addapter, 2)

    }

    private fun onclick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)
        binding.toolbar.card_back.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnAddAddress.setOnClickListener {
            MapBottomSheet.newInstance(null, object : OnAddressAddesClickLisener {
                override fun onAddressClickLisener() {
                    mViewModel.getAllAddresses()
                }

            }).show(childFragmentManager, MapBottomSheet::class.java.canonicalName)
        }
    }

    override fun onRemoveAddressClickLisener(item: Address, position: Int) {
        this.positionToDelete = position
        item?.let { mViewModel.deleteAddress(it) }
    }

    override fun onDefaultAddressClickLisener(item: Address, position: Int) {
        this.positionToDefault = position
        item?.let { mViewModel.changeCurrentAddressParam(it) }
    }

}