package com.dotjoo.aghsilinicustomer.ui.fragment.main.order

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseViewModel
import com.dotjoo.aghsilinicustomer.data.Param.CompalinParam
import com.dotjoo.aghsilinicustomer.data.Param.OrderInfoParam
import com.dotjoo.aghsilinicustomer.data.Param.ReviewParam
import com.dotjoo.aghsilinicustomer.data.remote.response.*
import com.dotjoo.aghsilinicustomer.domain.OrderActionUseCase
import com.dotjoo.aghsilinicustomer.domain.OrderUseCase
import com.dotjoo.aghsilinicustomer.domain.OrderUseCase.OrderTypes.CURRENT
import com.dotjoo.aghsilinicustomer.domain.OrderUseCase.OrderTypes.NEWw
import com.dotjoo.aghsilinicustomer.domain.OrderUseCase.OrderTypes.PREV
import com.dotjoo.aghsilinicustomer.util.NetworkConnectivity
import com.dotjoo.aghsilinicustomer.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrderViewModel
@Inject constructor(
    app: Application,
    val useCase: OrderUseCase,
    val useCaseOrderAction: OrderActionUseCase,
 ) : BaseViewModel<OrderAction>(app) {


var orderId:String? =null
    init {
        getCurrentOrder()
        getNewOrders()
        getPrevOrders()
    }

    fun getCurrentOrder() {

        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {

            produce(OrderAction.ShowLoading(true))
            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, CURRENT
                ) { res ->


                    when (res) {
                        is Resource.Failure -> {
                            produce(OrderAction.ShowFailureMsg(res.message.toString()))

                        }     is Resource.Progress -> produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success ->   produce(OrderAction.CurrentOrders(res.data?.data as AlOrdersResponse))

                    }

                }
            }

        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun getNewOrders() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, NEWw
                ) { res ->
                    when (res) {
                        is Resource.Failure -> {
                            produce(OrderAction.ShowFailureMsg(res.message.toString()))

                        }

                        is Resource.Progress ->produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success ->
                        produce(OrderAction.NewOrders(res.data?.data as AlOrdersResponse))

                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun getPrevOrders() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, PREV
                ) { res ->
                    when (res) {
                        is Resource.Failure -> {
                            produce(OrderAction.ShowFailureMsg(res.message.toString()))

                        }

                        is Resource.Progress ->produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success ->
                        produce(OrderAction.PrevOrders(res.data?.data as AlOrdersResponse))

                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun getOrderInfo(orderID: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, OrderInfoParam(orderID)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(OrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(OrderAction.OrderInfo(res.data?.data as OrderInfoResponse))
                        }
                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun reviewLaundry( order_id: String,
                       rate: String,
                       note: String,
                       laundry_id: String,) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseOrderAction.invoke(
                    viewModelScope, ReviewParam( order_id,
                        rate,
                        note,
                        laundry_id)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(OrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(OrderAction.OrderReviewed(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun complain( order_id: String,

                       note: String,
                       laundry_id: String,) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseOrderAction.invoke(
                    viewModelScope, CompalinParam( order_id,
                         note,
                        laundry_id)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(OrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(OrderAction.OrderComplained(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
 fun cancelOrder( order_id: String,
                    ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseOrderAction.invoke(
                    viewModelScope, OrderInfoParam( order_id,
                     )
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(OrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(OrderAction.OrderCanceled(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }


}






