package com.dotjoo.aghsilinicustomer.ui.fragment.main.orderInfo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Paint
import android.net.Uri
import androidx.core.content.ContextCompat.registerReceiver
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.data.remote.response.OrderInfo
import com.dotjoo.aghsilinicustomer.data.remote.response.OrderInfoItem
import com.dotjoo.aghsilinicustomer.data.remote.response.OrderInfoResponse
import com.dotjoo.aghsilinicustomer.databinding.FragmentOrderInfoBinding
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity.Companion.MAIN_SCREEN_ACTION
import com.dotjoo.aghsilinicustomer.ui.adapter.order.OrderInfoItemsAdapter
import com.dotjoo.aghsilinicustomer.ui.dialog.ComplainBotomSheetFragment
import com.dotjoo.aghsilinicustomer.ui.dialog.OnClickLoginFirst
import com.dotjoo.aghsilinicustomer.ui.dialog.RateBotomSheetFragment
import com.dotjoo.aghsilinicustomer.ui.fragment.main.order.OrderAction
import com.dotjoo.aghsilinicustomer.ui.fragment.main.order.OrderViewModel
import com.dotjoo.aghsilinicustomer.util.Constants
import com.dotjoo.aghsilinicustomer.util.SimpleDividerItemDecoration
import com.dotjoo.aghsilinicustomer.util.ext.getMyData
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.ext.init
import com.dotjoo.aghsilinicustomer.util.ext.loadImage
import com.dotjoo.aghsilinicustomer.util.observe
import com.google.android.gms.tasks.Tasks.call
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderInfoFragment : BaseFragment<FragmentOrderInfoBinding>() {
    val mViewModel: OrderViewModel by activityViewModels()
    lateinit var adapter: OrderInfoItemsAdapter
    var list = arrayListOf<OrderInfoItem>()
    var orderId:String ? = null
    var orderInfo:OrderInfoResponse ? = null
    private val reciever = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            var data: String? = intent.getMyData<String>(Constants.Notifaction)
            if (data == null) {
            } else {
                   orderId = data
                orderId?.let { mViewModel.getOrderInfo(it) }

            }
        }
    }

    override fun onFragmentReady() {
        initAdapters()
        onClick()
       requireContext(). registerReceiver(reciever, IntentFilter(MAIN_SCREEN_ACTION))
        if((arguments?.getString(Constants.ORDER_ID )!= null)){
               orderId =arguments?.getString(Constants.ORDER_ID)
           }
           else{
           orderId = mViewModel.orderId
       }
         orderId?.let { mViewModel.getOrderInfo(it) }

        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            orderId?.let { mViewModel.getOrderInfo(it) }
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
                }else if (it.contains("aghsilini.com") == true) {
                    showToast(resources.getString(R.string.connection_error))
                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is OrderAction.OrderInfo -> {
                loadOrderData(action.data)
            }
 is OrderAction.OrderCanceled -> {
findNavController().navigateUp()            }

            else -> {

            }
        }
    }

    private fun loadOrderData(data: OrderInfoResponse) {
        data?.let {
            orderInfo=it
            binding.lytData.isVisible = true
        binding.tv1.isVisible= true
            adapter.ordersList = it.order?.orderitems!!
            adapter.notifyDataSetChanged()
            binding.tvDeliveryFeesValue.setText(it.order?.delivery + " " + resources.getString(R.string.sr))
            binding.tvStatus.setText(it.order?.progress)
            binding.tvAddressUser.setText(it.order?.address)
            binding.tvSubTotalValue.setText(it?.totalItemsPrice + " " + resources.getString(R.string.sr))
            binding.tvTotalValue.setText(it.order?.total + " " + resources.getString(R.string.sr))
            binding.tvTaxValue.setText(it.order?.tax + " " + resources.getString(R.string.sr))
            binding.tvAddValue.setText(it.order?.additional_cost + " " + resources.getString(R.string.sr))
            binding.tvUrgent.isVisible = (it.order?.argent == 1)
            binding.tvLaundryName.setText(it?.order?.laundry?.name)
            binding.tvAddress.setText(it?.order?.laundry?.address)
            binding.tvRateCount.setText(it?.order?.laundry?.rate)
            binding.ivlogo.loadImage(it?.order?.laundry?.logo)
          binding.cardCall.setOnClickListener {
              data?.order?.laundry?.phone?.let{
                  call(it)
              }
          }
            if(it.order?.payment_type==1) binding.tvPayment.setText(R.string.wallet)
            binding.tvCancel.isVisible = it.order?.cancelation == true
             handleStatus(it.order?.progress  )
        }
    }
    private fun handleStatus(progress: String? ) {
        when (progress) {
            Constants.NEW -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.order_status_new))
                binding.tvStatus.setText(resources.getString(R.string.new_order))

            }
            Constants.WAITING_DRIVER -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state2))
                binding.tvStatus.setText(resources.getString(R.string.driver_on_way))

            }
            Constants.DRIVER_IN_WAY -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state2))
                binding.tvStatus.setText(resources.getString(R.string.driver_on_way))

            }
            Constants.DRIVER_RECIVE_FROM_CUSTOMER -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state2))
                binding.tvStatus.setText(resources.getString(R.string.driver_on_way))

            }


            Constants.LAUNDRY_RECIVE -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state3))
                binding.tvStatus.setText(resources.getString(R.string.laundry_recive))

            }
            Constants.START_PREPARE -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state3))
                binding.tvStatus.setText(resources.getString(R.string.preparied_now))

            }
            Constants.END_PREPARED -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state4))
                binding.tvStatus.setText(resources.getString(R.string.preparied))

            }
            Constants.DRIVER_RECIVE_FROM_LAUNDRY -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state5))
                binding.tvStatus.setText(resources.getString(R.string.driver_on_way))

            }
            Constants.DRIVER_IN_WAY_TO_LAUNDRY -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state5))
                binding.tvStatus.setText(resources.getString(R.string.driver_on_way))

            }
            Constants.COMPLETED -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state6))
                binding.tvStatus.setText(resources.getString(R.string.compeleted))
                binding.tvRate.isVisible = (orderInfo?.order?.review == true)

            }

            else -> {

            }
        }
    }


    fun call(tel: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:" + tel)
        startActivity(dialIntent)
    }

    lateinit var parent: MainActivity
    private fun onClick() {
        binding.tvRate.setPaintFlags(binding.tvRate.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)
        binding.tvRate.setOnClickListener {
            orderId?.let { it1 ->
                orderInfo?.order?.laundry?.let { it2 ->
                    RateBotomSheetFragment.newInstance(
                        it2,
                        it1, object :OnClickLoginFirst{
                            override fun onClick(choice: String) {
                           mViewModel.getOrderInfo(     it1 )                        }

                        }
                    ).show(childFragmentManager, ComplainBotomSheetFragment::class.java.canonicalName)
                }
            }        }
      binding.tvCancel.setOnClickListener {
          orderId?.let { it1 -> mViewModel.cancelOrder(it1) }
      }
        binding.cardComplains.setOnClickListener {
             orderId?.let { it1 ->
                orderInfo?.order?.laundry?.let { it2 ->
                    ComplainBotomSheetFragment.newInstance(
                        it2,
                        it1
                    ).show(childFragmentManager, ComplainBotomSheetFragment::class.java.canonicalName)
                }
            }
        }
        binding.cardBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initAdapters() {
        adapter = OrderInfoItemsAdapter()
        binding.rvOrders.init(requireContext(), adapter, 2)
        binding.rvOrders.addItemDecoration(SimpleDividerItemDecoration(requireContext()))


    }

}