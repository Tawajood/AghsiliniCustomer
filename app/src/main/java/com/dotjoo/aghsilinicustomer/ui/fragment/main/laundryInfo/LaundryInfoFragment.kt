package com.dotjoo.aghsilinicustomer.ui.fragment.main.laundryInfo

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.data.Param.AddToCartParam
import com.dotjoo.aghsilinicustomer.data.Param.Items
import com.dotjoo.aghsilinicustomer.data.remote.response.Cartitems
import com.dotjoo.aghsilinicustomer.data.remote.response.ItemsInService
import com.dotjoo.aghsilinicustomer.data.remote.response.Laundry
import com.dotjoo.aghsilinicustomer.data.remote.response.ServiceInLaundry
import com.dotjoo.aghsilinicustomer.data.remote.response.ServiceResponse
import com.dotjoo.aghsilinicustomer.databinding.FragmentLaundryInfoBinding
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.adapter.laundry_info.ItemsAdapter
import com.dotjoo.aghsilinicustomer.ui.adapter.laundry_info.ServiceAdapter
import com.dotjoo.aghsilinicustomer.ui.fragment.main.basket.CreateOrderAction
import com.dotjoo.aghsilinicustomer.ui.fragment.main.basket.CreateOrderViewModel
import com.dotjoo.aghsilinicustomer.ui.lisener.ItemsInLaundryClickListener
import com.dotjoo.aghsilinicustomer.ui.lisener.ServiceClickListener
import com.dotjoo.aghsilinicustomer.util.Constants
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.ext.init
import com.dotjoo.aghsilinicustomer.util.ext.loadImage
import com.dotjoo.aghsilinicustomer.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaundryInfoFragment : BaseFragment<FragmentLaundryInfoBinding>(), ServiceClickListener,
    ItemsInLaundryClickListener {

    val mViewModel: CreateOrderViewModel by activityViewModels()
    lateinit var parent: MainActivity

    lateinit var adapterServices: ServiceAdapter
    lateinit var adapterItems: ItemsAdapter
    var launary: Laundry? = null
    var list: ArrayList<ItemsInService> = arrayListOf()
    private var currentService: ServiceInLaundry? = null
    var price = 0.0

    override fun onFragmentReady() {

        onClick()
        initAdapters()
        launary = arguments?.getParcelable(Constants.Laundry)
        setupLaundry()
        mViewModel.apply {
            launary?.id?.let { getServices(it) }
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            launary?.id?.let { mViewModel.getServices(it) }
            if (binding.swiperefreshHome != null) binding.swiperefreshHome.isRefreshing = false
        }
    }

    private fun setupLaundry() {
        launary?.let {
            binding.tvLaundryName.setText(it.name)
            binding.tvAddress.setText(it.address)
              binding.tvRate.setText(it.rate)
            binding.ivLogo.loadImage(it.logo, isCircular = true)
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

            is CreateOrderAction.ShowServices -> {
                loadServices(action.data)
                currentService = action.data.services.get(0)
                list = currentService?.items ?: arrayListOf()
                list?.let { loadItems(it) }


            }



            is CreateOrderAction.AddedToCart -> {

                findNavController().navigate(R.id.basketkFragment)


            }

            else -> {

            }
        }
    }

    private fun loadServices(data: ServiceResponse) {

        data.services?.get(0)?.choosen = true
        adapterServices.ordersList = data.services
        adapterServices.notifyDataSetChanged()

    }


    private fun onClick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)

        binding.titleRegular.setOnClickListener {
            stateRegular()
        }
        binding.cardBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.titleUrgent.setOnClickListener {
            stateUrgent()
        }
        binding.tvRate.setOnClickListener {
            findNavController().navigate(R.id.laundrRateFragment, bundleOf(Constants.Laundry to launary))

        }
        binding.tvAddtoBasket.setOnClickListener {
            var list = arrayListOf<Items>()
            if(mViewModel.urgent)   orderlist.forEach {
                list.add(Items(it.key, it.value.count, it.value.argentPrice))
            }
                else
            orderlist.forEach {
                list.add(Items(it.key, it.value.count, it.value.price))
            }
            mViewModel.addToCart(AddToCartParam(launary?.id ,mViewModel.urgent,list))
        }
    }

    private fun stateUrgent() {
        binding.titleRegular.background = resources.getDrawable(R.color.white)
        binding.titleUrgent.background = resources.getDrawable(R.drawable.bg_blue)
        binding.titleUrgent.setTextColor(resources.getColor(R.color.white))
        binding.titleRegular.setTextColor(resources.getColor(R.color.blue))
        adapterItems.ordersList = arrayListOf()
        mViewModel.urgent = true
        loadItems(list)
        calaculatePrice()
    }

    private fun stateRegular() {
        binding.titleUrgent.background = resources.getDrawable(R.color.white)
        binding.titleRegular.background = resources.getDrawable(R.drawable.bg_blue)
        binding.titleRegular.setTextColor(resources.getColor(R.color.white))
        binding.titleUrgent.setTextColor(resources.getColor(R.color.blue))
        adapterItems.ordersList = arrayListOf()
        mViewModel.urgent = false
        loadItems(list)
        calaculatePrice()
    }

    private fun initAdapters() {
        adapterServices = ServiceAdapter(this)
        adapterItems = ItemsAdapter(this)
        binding.rvItems.init(requireContext(), adapterItems, 2)
        binding.rvServices.init(requireContext(), adapterServices, 1)

    }

    private fun loadItems(list: ArrayList<ItemsInService>) {
        if (list.size > 0) {
            adapterItems.ordersList = list
            adapterItems.urgent = mViewModel.urgent
            adapterItems.notifyDataSetChanged()
            binding.lytEmptyState.isVisible = false
            binding.rvItems.isVisible = true

        } else {
            binding.lytEmptyState.isVisible = true
            binding.rvItems.isVisible = false

        }


    }

    var orderlist: HashMap<String, ItemsInService> = hashMapOf()
    var count = 0


    private fun showBasketAddition(count: Int) {
        if (count > 0) {
            binding.tvCount.text = count.toString() + resources.getString(R.string.items)
            binding.tvPrice.text = (count * 15).toString() + resources.getString(R.string.sar)
            binding.tvPrice.setText(price.toString() + resources.getString(R.string.sar))
            binding.lytBasket.isVisible = true

        } else {
            binding.lytBasket.isVisible = false

        }
    }

    override fun onServiceClickLisener(item: ServiceInLaundry) {
        adapterItems.ordersList = arrayListOf()
        adapterItems.notifyDataSetChanged()
        currentService = item
        list = currentService?.items  ?: arrayListOf()
        list?.let { loadItems(it) }
    }

    override fun onItemsClickLisener(item: ItemsInService, plus: Int?) {
        count = 0
        price = 0.0
        if (item.count == 0) {
            orderlist.remove(item.id, item)

        }
        orderlist.put(item.id.toString(), item)


      calaculatePrice()



    }

    private fun calaculatePrice() {
        count = 0
        price = 0.0

        if( mViewModel.urgent) {
            for (i in orderlist) {
                i.value.count?.let {
                    count = count.plus(it)
                    i.value.argentPrice?.toDoubleOrNull()
                        ?.let { it1 -> price = price.plus(it1 * i.value.count!!) }
                }
            }
        }else{


            for (i in orderlist) {
                i.value.count?.let {
                    count = count.plus(it)
                    i.value.price?.toDoubleOrNull()
                        ?.let { it1 -> price = price.plus(it1 * i.value.count!!) }
                }
            }
        }
        showBasketAddition(count)    }


}