package com.dotjoo.aghsilinicustomer.ui.fragment.main.checkout

import android.graphics.Paint
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.data.Param.CreateOrderParam
import com.dotjoo.aghsilinicustomer.data.remote.response.Address
import com.dotjoo.aghsilinicustomer.data.remote.response.Cartitems
import com.dotjoo.aghsilinicustomer.databinding.FragmentCheckoutBinding
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.adapter.checkout.AddressAdapter
import com.dotjoo.aghsilinicustomer.ui.adapter.checkout.ItemsCheckoutAdapter
import com.dotjoo.aghsilinicustomer.ui.dialog.AddBalanceSheetFragment
import com.dotjoo.aghsilinicustomer.ui.dialog.OnClickLoginFirst
import com.dotjoo.aghsilinicustomer.ui.fragment.auth.register.MapBottomSheet
import com.dotjoo.aghsilinicustomer.ui.fragment.main.basket.CreateOrderAction
import com.dotjoo.aghsilinicustomer.ui.fragment.main.basket.CreateOrderViewModel
import com.dotjoo.aghsilinicustomer.ui.lisener.OnAddressAddesClickLisener
import com.dotjoo.aghsilinicustomer.ui.lisener.OnAddressClickLisener
import com.dotjoo.aghsilinicustomer.util.Constants
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.ext.init
import com.dotjoo.aghsilinicustomer.util.ext.isNull
import com.dotjoo.aghsilinicustomer.util.ext.loadImage
import com.dotjoo.aghsilinicustomer.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.card_back
import kotlinx.android.synthetic.main.toolbar.view.tv_title
import kotlinx.coroutines.delay

@AndroidEntryPoint
class CheckoutFragment : BaseFragment<FragmentCheckoutBinding>(), OnAddressClickLisener {

    lateinit var parent: MainActivity
    val mViewModel: CreateOrderViewModel by activityViewModels()

    lateinit var adapter: ItemsCheckoutAdapter
    lateinit var adapterAddress: AddressAdapter

    var address: Address? = null
    var laundry_id: String? = null
    var totalItemsPrice: String? = null
    var tax: String? = null
    var delivery: String? = null
    var total: String? = null
    var urgent: Int? = null
    var paymentType: Int? = null


    override fun onFragmentReady() {
        setupUi()
        onClick()
        initAdapters()
        mViewModel.apply {
            getCart()
            getAllAddresses()
            getWallet()
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getCart()
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

                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is CreateOrderAction.AllAddressShown -> {
                action.data.address?.let {
                    adapterAddress.ordersList = it

                    adapterAddress.notifyDataSetChanged()
                }
            }

            is CreateOrderAction.OrderCreated -> {
                mViewModel.order_id = action.data?.order?.id
                findNavController().navigate(
                    R.id.successfulOrderFragment, null,
                    NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build()
                )

            }

            is CreateOrderAction.ShowBalanceInWallet -> {
                binding.tvWalletBalance.setText(action.data?.wallet + resources.getText(R.string.sr))
                binding.tvWalletBalance.isVisible = true
            }

            is CreateOrderAction.ShowCart -> {
                laundry_id = action.data.laundry_id
                totalItemsPrice = action.data.totalItemsPrice
                tax = action.data.tax
                delivery = action.data.delivery
                total = action.data.total
                urgent = action.data.urgent
              binding.tvTaxValue.setText(tax +resources.getString(R.string.sr))
              binding.tvDeliveryFeesValue.setText(delivery +resources.getString(R.string.sr))
              binding.tvTotalValue.setText(total +resources.getString(R.string.sr))
              binding.tvSubTotalValue.setText(totalItemsPrice +resources.getString(R.string.sr))


                action.data.laundry?.let {
                    binding.tvLaundryName.setText(it.name)
                    binding.tvAddress.setText(it.address)
                    binding.tvRateCount.setText(it.rate)
                    binding.ivlogo.loadImage(it.logo, isCircular = true)
                }
                loadItems(action.data.cartitems)
            }

            else -> {

            }
        }

    }


    private fun loadItems(list: ArrayList<Cartitems>) {
        if (list.size > 0) {
            adapter.ordersList = list
            adapter.notifyDataSetChanged()
            binding.lytData.isVisible = true
        } else {
            binding.lytData.isVisible = false

        }
    }

    private fun initAdapters() {
        adapter = ItemsCheckoutAdapter()
        binding.rvOrders.init(requireContext(), adapter, 2)
        adapterAddress = AddressAdapter(this)
        binding.rvAddress.init(requireContext(), adapterAddress, 1)
    }

    private fun setupUi() {
        binding.tvAddAddress.setPaintFlags(binding.tvAddAddress.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)
        binding.toolbar.tv_title.setText(resources.getString(R.string.confirm_order))
    }

    private fun onClick() {

        binding.cardPay.setOnClickListener {
            //    createOrder
            if (address == null) showToast(resources.getString(R.string.choose_add_first))
            else if (paymentType == null) showToast(resources.getString(R.string.choose_payment_type))
            else {
                mViewModel.createOrder(
                    CreateOrderParam(
                        address?.lat!!, address?.lon!!, address?.address,
                        laundry_id,
                        total,
                        urgent == 1,
                        tax,
                        delivery,
                        paymentType
                    )
                )
            }
        }
        binding.tvAddAddress.setOnClickListener {

            MapBottomSheet.newInstance(null, object : OnAddressAddesClickLisener {
                override fun onAddressClickLisener() {
                    mViewModel.getAllAddresses()
                    address = null
                }

            }).show(childFragmentManager, MapBottomSheet::class.java.canonicalName)
        }
        binding.toolbar.card_back.setOnClickListener {
            findNavController().navigateUp()
        }


        binding.checboxEPay.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.checboxPaywallet.isChecked = false
                paymentType = Constants.visa
             } else {
                paymentType = null

            }
        }
        binding.checboxPaywallet.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.checboxEPay.isChecked = false
                paymentType = Constants.wallet

            } else {
                paymentType = null
             }
        }
        binding.ivAddWallet.setOnClickListener {
            AddBalanceSheetFragment.newInstance(object : OnClickLoginFirst {
                override fun onClick(choice: String) {

                    mViewModel.getWallet()
                }

            }).show(childFragmentManager, AddBalanceSheetFragment::class.java.canonicalName)
        }
    }


    override fun onAddressClickLisener(item: Address) {
        address = item

    }

}
