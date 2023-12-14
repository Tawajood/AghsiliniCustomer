package com.dotjoo.aghsilinicustomer.ui.fragment.main.order

 import com.dotjoo.aghsilinicustomer.base.Action
 import com.dotjoo.aghsilinicustomer.data.remote.response.AlOrdersResponse
 import com.dotjoo.aghsilinicustomer.data.remote.response.AllLaundriesResponse
 import com.dotjoo.aghsilinicustomer.data.remote.response.OrderInfoResponse

sealed class OrderAction : Action {

    data class ShowLoading(val show: Boolean) : OrderAction()
    data class ShowFailureMsg(val message: String?) : OrderAction()


     data class  OrderInfo(val data : OrderInfoResponse): OrderAction ()
    data class  NewOrders(val data : AlOrdersResponse): OrderAction ()
    data class  PrevOrders(val data : AlOrdersResponse): OrderAction ()
    data class  CurrentOrders(val data : AlOrdersResponse): OrderAction ()
    data class  OrderReviewed(val message: String? ): OrderAction ()
    data class  OrderComplained(val message : String): OrderAction ()
    data class  OrderCanceled(val message : String): OrderAction ()


}
