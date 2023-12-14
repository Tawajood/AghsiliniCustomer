package com.dotjoo.aghsilinicustomer.ui.fragment.main.basket

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dotjoo.aghsilinicustomer.R
 import com.dotjoo.aghsilinicustomer.base.BaseViewModel
import com.dotjoo.aghsilinicustomer.data.Param.AddAddressParam
import com.dotjoo.aghsilinicustomer.data.Param.AddToCartParam
import com.dotjoo.aghsilinicustomer.data.Param.CreateOrderParam
import com.dotjoo.aghsilinicustomer.data.Param.DeleteAddressParam
import com.dotjoo.aghsilinicustomer.data.Param.GetItemsInServiceParam
import com.dotjoo.aghsilinicustomer.data.Param.IncreaseItemParam
import com.dotjoo.aghsilinicustomer.data.Param.RemoveItemParam
import com.dotjoo.aghsilinicustomer.data.remote.response.*
import com.dotjoo.aghsilinicustomer.domain.BasketUseCase
import com.dotjoo.aghsilinicustomer.domain.BasketUseCase.OrderTypes.ALL_ADDRESS
import com.dotjoo.aghsilinicustomer.domain.CreateOrderUseCase
import com.dotjoo.aghsilinicustomer.domain.SettingWalletUseCase
import com.dotjoo.aghsilinicustomer.util.NetworkConnectivity
import com.dotjoo.aghsilinicustomer.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateOrderViewModel
@Inject constructor(app: Application, val useCase:CreateOrderUseCase, val useCaseWallet:SettingWalletUseCase, val useCase_basket: BasketUseCase) :
    BaseViewModel<CreateOrderAction>(app) {
     var urgent = false
     var order_id :String? = null

    fun getCart( ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(CreateOrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase_basket.invoke(
                    viewModelScope )
                { res ->
                    when (res) {
                      is Resource.Failure -> produce(CreateOrderAction.ShowFailureMsg(res.message.toString()))
                      is Resource.Progress -> produce(CreateOrderAction.ShowLoading(res.loading))
                      is Resource.Success -> {
                          produce(CreateOrderAction.ShowCart(res.data?.data as CartResponse))
                      }
                  }
              }
            }
        }
        else {
            produce(CreateOrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

   fun getServices( laundry_id :String  ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(CreateOrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope ,GetItemsInServiceParam( laundry_id  ))
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(CreateOrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(CreateOrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(CreateOrderAction.ShowServices(res.data?.data as ServiceResponse))
                        }
                    }
                }
            }
        }
        else {
            produce(CreateOrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
 fun increaseItem(item: Any, itemId: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(CreateOrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope , IncreaseItemParam( itemId , true)
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(CreateOrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(CreateOrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(CreateOrderAction.ShowItemsIncrease(res.data?.message as String, item))
                        }
                    }
                }
            }
        }
        else {
            produce(CreateOrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun removeItem(item: Cartitems, position: Int) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(CreateOrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope , RemoveItemParam(item.itemId.toString())
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(CreateOrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(CreateOrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(CreateOrderAction.ShowItemsRemoved(res.data?.message as String, item, position))
                        }
                    }
                }
            }
        }
        else {
            produce(CreateOrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
fun decreaseItem(   itemId :String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(CreateOrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope , IncreaseItemParam( itemId , false))
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(CreateOrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(CreateOrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(CreateOrderAction.ShowItemsDecrease(res.data?. message as String))
                        }
                    }
                }
            }
        }
        else {
            produce(CreateOrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
fun addToCart(   param : AddToCartParam) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(CreateOrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase_basket.invoke(
                    viewModelScope ,param)
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(CreateOrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(CreateOrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(CreateOrderAction.AddedToCart(res.data?. message as String))
                        }
                    }
                }
            }
        }
        else {
            produce(CreateOrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
fun createOrder(   param : CreateOrderParam) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(CreateOrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase_basket.invoke(
                    viewModelScope ,param)
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(CreateOrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(CreateOrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(CreateOrderAction.OrderCreated(res.data?. data as CreateOrderResponse))
                        }
                    }
                }
            }
        }
        else {
            produce(CreateOrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
fun getAllAddresses(    ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(CreateOrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase_basket.invoke(
                    viewModelScope , ALL_ADDRESS
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(CreateOrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(CreateOrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(CreateOrderAction.AllAddressShown(res.data?.data   as AllAddressResponse))
                        }
                    }
                }
            }
        }
        else {
            produce(CreateOrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun addAddress( param: AddAddressParam) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(CreateOrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase_basket.invoke(
                    viewModelScope ,param
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(CreateOrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(CreateOrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(CreateOrderAction.AddressAdded(res.data?.message   as String))
                        }
                    }
                }
            }
        }
        else {
            produce(CreateOrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

     fun deleteAddress( item: Address) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(CreateOrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase_basket.invoke(
                    viewModelScope , item.id?.let { DeleteAddressParam(it) }
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(CreateOrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(CreateOrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(CreateOrderAction.AddressDeleted(res.data?.message   as String, item))
                        }
                    }
                }
            }
        }
        else {
            produce(CreateOrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun getWallet(  ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(CreateOrderAction.ShowLoading(true))

            viewModelScope.launch {
                useCaseWallet  .invoke(
                    viewModelScope
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(CreateOrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(CreateOrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(CreateOrderAction.ShowBalanceInWallet(res.data?.data as WalletResponse))
                        }
                    }
                }
            }
        }
        else {
            produce(CreateOrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    }
