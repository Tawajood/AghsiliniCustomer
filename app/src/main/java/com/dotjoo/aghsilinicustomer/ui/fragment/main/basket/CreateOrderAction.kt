package com.dotjoo.aghsilinicustomer.ui.fragment.main.basket

import com.dotjoo.aghsilinicustomer.base.Action
import com.dotjoo.aghsilinicustomer.data.remote.response.Address
import com.dotjoo.aghsilinicustomer.data.remote.response.AllAddressResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.CartResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.CreateOrderResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.ServiceResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.WalletResponse



sealed class CreateOrderAction : Action {

    data class ShowLoading(val show: Boolean) : CreateOrderAction()
    data class ShowFailureMsg(val message: String?) : CreateOrderAction()
    data class ShowItemsDecrease(val message: String?) : CreateOrderAction()
    data class ShowItemsIncrease(val message: String?, val item: Any) : CreateOrderAction()
    data class ShowItemsRemoved(val message: String?, val item: Any, val position: Int) : CreateOrderAction()
    data class AddedToCart(val message: String?) : CreateOrderAction()
    data class OrderCreated(val data: CreateOrderResponse?) : CreateOrderAction()
    data class ShowBalanceInWallet(val data: WalletResponse?) : CreateOrderAction()

      data class AddressAdded(var msg: String ) : CreateOrderAction()
      data class AddressDeleted(var msg: String, val item: Address) : CreateOrderAction()
     data class  ShowCart(val data : CartResponse): CreateOrderAction ()
     data class  AllAddressShown(val data : AllAddressResponse): CreateOrderAction ()
    data   class ShowServices(val data: ServiceResponse) : CreateOrderAction()


}
