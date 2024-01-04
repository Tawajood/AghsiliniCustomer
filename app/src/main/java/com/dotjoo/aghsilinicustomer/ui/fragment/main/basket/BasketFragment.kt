package com.dotjoo.aghsilinicustomer.ui.fragment.main.basket

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.data.remote.response.CartResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.Cartitems
import com.dotjoo.aghsilinicustomer.data.remote.response.Laundry
import com.dotjoo.aghsilinicustomer.databinding.FragmentBasketBinding
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.adapter.BasketAdapter
import com.dotjoo.aghsilinicustomer.ui.lisener.ItemsInBasketClickListener
import com.dotjoo.aghsilinicustomer.util.Constants
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.ext.init
import com.dotjoo.aghsilinicustomer.util.observe
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BasketFragment : BaseFragment<FragmentBasketBinding>(), ItemsInBasketClickListener {
    lateinit var adapter: BasketAdapter
    var list = arrayListOf<Laundry>()
    val mViewModel: CreateOrderViewModel by viewModels()
    lateinit var parent: MainActivity

    override fun onFragmentReady() {
        onClick()
        initAdapters()

        mViewModel.apply {
            getCart()
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getCart()
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
                }else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is CreateOrderAction.ShowCart -> {
                loadCart(action.data)
            }
            is CreateOrderAction.ShowItemsRemoved -> {
adapter.removeItem(action.position)
                mViewModel.getCart()

            }
           is CreateOrderAction.ShowItemsIncrease -> {
                 mViewModel.getCart()

            }
           is CreateOrderAction.ShowItemsDecrease -> {
                 mViewModel.getCart()

            }
            else -> {

            }
        }
    }

    private fun loadCart(data: CartResponse) {
        binding.tvDeliveryFeesValue.setText(data.delivery + " " + resources.getString(R.string.sr))
        binding.tvSubTotalValue.setText(data.totalItemsPrice + " " + resources.getString(R.string.sr))
        binding.tvTotalValue.setText(data.total + " " + resources.getString(R.string.sr))
        binding.tvTaxValue.setText(data.tax + " " + resources.getString(R.string.sr))
binding.cardUrgent.isVisible= data.urgent==1
        loadItems(data.cartitems)
     var count =0
        data.cartitems.forEach {
            count =count + it.count
        }
        binding.tvItemsCountValue.setText(count.toString()+" "+ resources.getText(R.string.items))
     }



    private fun loadItems(list: ArrayList<Cartitems>) {
        if (list.size > 0) {
            adapter.ordersList = list
            adapter.notifyDataSetChanged()
            binding.lytData.isVisible= true
            binding.lytEmptyState.isVisible= false
        }else{
            binding.lytData.isVisible= false
            binding.lytEmptyState.isVisible= true

        }
    }
    private fun onClick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(true)
        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.checkoutFragment)
        }
   binding.btnMakeOrder.setOnClickListener {
       findNavController().navigate(
           R.id.homeFragment,
           null,
           NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build()
       )        }
    }


    private fun initAdapters() {
        adapter = BasketAdapter(this)
        binding.rvOrders.init(requireContext(), adapter, 2)

    }

    override fun onItemsClickLisener(item: Cartitems, plus: Int) {

        if (plus == Constants.PLUS) {
            item.itemId?.let { mViewModel.increaseItem(item, it) }
        } else {
            item.itemId?.let { mViewModel.decreaseItem(it) }

        }    }

    override fun onItemsRemoveLisener(item: Cartitems, position: Int) {
mViewModel.removeItem(item, position)    }


}