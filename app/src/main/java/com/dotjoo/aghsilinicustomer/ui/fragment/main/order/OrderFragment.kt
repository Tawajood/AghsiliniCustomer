package com.dotjoo.aghsilinicustomer.ui.fragment.main.order

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.data.remote.response.Order
import com.dotjoo.aghsilinicustomer.databinding.FragmentOrderBinding
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.adapter.order.OrderAdapter
import com.dotjoo.aghsilinicustomer.ui.adapter.order.OrderType.CURRNET
import com.dotjoo.aghsilinicustomer.ui.adapter.order.OrderType.FINISHED
import com.dotjoo.aghsilinicustomer.ui.adapter.order.OrderType.NEW
import com.dotjoo.aghsilinicustomer.ui.lisener.OnOrderClickListener
import com.dotjoo.aghsilinicustomer.util.Resource
import com.dotjoo.aghsilinicustomer.util.ToastUtils
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.ext.init
import com.dotjoo.aghsilinicustomer.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.card_back
import kotlinx.android.synthetic.main.toolbar.view.tv_title
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint

class OrderFragment : BaseFragment<FragmentOrderBinding>(), OnOrderClickListener {
    lateinit var adapter: OrderAdapter
    val mViewModel: OrderViewModel by activityViewModels()
    var listNew = arrayListOf<Order>()
    var listCurrent = arrayListOf<Order>()
    var listPrev = arrayListOf<Order>()
    var state= NEW
    override fun onFragmentReady() {
        initAdapters()
        onClick()
        mViewModel.apply {
            getCurrentOrder()
            getNewOrders()
            getPrevOrders( )
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {

            mViewModel.getCurrentOrder()
            mViewModel.getNewOrders()
           mViewModel.getPrevOrders( )
            if (binding.swiperefreshHome != null) binding.swiperefreshHome.isRefreshing = false
        }
    }
    fun handleViewState(action: OrderAction) {
        when (action) {
            is OrderAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }


            is OrderAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)
                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }


            is OrderAction.NewOrders -> {
                listNew= action.data.orders
                if(state==NEW)    stateNew()

            }
            is OrderAction.CurrentOrders -> {
                listCurrent= action.data.orders
                if(state==CURRNET)    stateCurrent()
            }
            is OrderAction.PrevOrders -> {
                listPrev= action.data.orders
                if(state==FINISHED)   stateFinished()
            }

            else -> {

            }
        }
    }


    lateinit var parent: MainActivity
    private fun onClick() {
        binding.toolbar.card_back.isVisible = false
        parent = requireActivity() as MainActivity
        parent.showBottomNav(true)
    }

    private fun initAdapters() {
        binding.toolbar.tv_title.setText(resources.getString(R.string.orders))
        adapter = OrderAdapter(this)
        binding.rvOrders.init(requireContext(), adapter, 2)


        setupClick()



    }

    private fun setupClick() {
        binding.titleCurrent.setOnClickListener {
            stateCurrent()
        }
        binding.titleFinished.setOnClickListener {
            stateFinished()
        }
        binding.titleNew.setOnClickListener {
            stateNew()
        }
        binding.btnMakeOrder.setOnClickListener {
             findNavController().navigate(
                R.id.homeFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build()
            )   }
    }


    private fun stateCurrent() {
        binding.titleCurrent.background = resources.getDrawable(R.drawable.bg_blue)
        binding.titleNew.background = resources.getDrawable(R.color.white)
        binding.titleFinished.background = resources.getDrawable(R.color.white)
        binding.titleCurrent.setTextColor(resources.getColor(R.color.white))
        binding.titleNew.setTextColor(resources.getColor(R.color.blue))
        binding.titleFinished.setTextColor(resources.getColor(R.color.blue))
        loadLaundries(listCurrent, CURRNET)
state= CURRNET
    }

    private fun stateNew() {
        binding.titleCurrent.background = resources.getDrawable(R.color.white)
        binding.titleNew.background = resources.getDrawable(R.drawable.bg_blue)
        binding.titleFinished.background = resources.getDrawable(R.color.white)
        binding.titleCurrent.setTextColor(resources.getColor(R.color.blue))
        binding.titleNew.setTextColor(resources.getColor(R.color.white))
        binding.titleFinished.setTextColor(resources.getColor(R.color.blue))
        state= NEW
        loadLaundries(listNew, NEW)
        // adapter.ordersList = arrayListOf()
        //   loadLaundries(list, NEW)
    }

    private fun stateFinished() {
        binding.titleCurrent.background = resources.getDrawable(R.color.white)
        binding.titleNew.background = resources.getDrawable(R.color.white)
        binding.titleFinished.background = resources.getDrawable(R.drawable.bg_blue)
        binding.titleCurrent.setTextColor(resources.getColor(R.color.blue))
        binding.titleNew.setTextColor(resources.getColor(R.color.blue))
        binding.titleFinished.setTextColor(resources.getColor(R.color.white))
        state= FINISHED
        loadLaundries(listPrev, FINISHED)
        //  adapter.ordersList = arrayListOf()
        //  loadLaundries(list, FINISHED)
    }

    private fun loadLaundries(list: ArrayList<Order>, type: Int) {
        if (list.size > 0) {
            adapter.ordersList = list
            adapter.type = type
            binding.lytEmptyState.isVisible = false
            adapter.notifyDataSetChanged()
        } else {
            adapter.ordersList = list
            adapter.type = type
            adapter.notifyDataSetChanged()
            when (type) {

                NEW -> {
                    binding.tvMsgNoLaundries.setText(resources.getString(R.string.no_orders_new))
                }

                CURRNET -> {
                    binding.tvMsgNoLaundries.setText(resources.getString(R.string.no_orders_current))
                }

                FINISHED -> {
                    binding.tvMsgNoLaundries.setText(resources.getString(R.string.no_orders_before))

                }

            }
            binding.lytEmptyState.isVisible = true
        }

    }

    override fun onItemsClickLisener(item: Order) {
        mViewModel.orderId = item.id

        findNavController().navigate(R.id.orderInfoFragment)
    }


}