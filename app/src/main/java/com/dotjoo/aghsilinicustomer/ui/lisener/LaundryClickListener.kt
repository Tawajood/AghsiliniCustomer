package com.dotjoo.aghsilinicustomer.ui.lisener

import com.dotjoo.aghsilinicustomer.data.remote.response.Address
import com.dotjoo.aghsilinicustomer.data.remote.response.Cartitems
import com.dotjoo.aghsilinicustomer.data.remote.response.ItemsInService
import com.dotjoo.aghsilinicustomer.data.remote.response.Laundry
import com.dotjoo.aghsilinicustomer.data.remote.response.Order
import com.dotjoo.aghsilinicustomer.data.remote.response.ServiceInLaundry

interface LaundryClickListener {
    fun onInfoClickLisener(item: Laundry)
    fun onRateClickLisener(item: Laundry)

}
interface ItemsInBasketClickListener {
    fun onItemsClickLisener(item: Cartitems, plus: Int)
    fun onItemsRemoveLisener(item: Cartitems, position: Int)

}
interface OnAddressClickLisener {
    fun onAddressClickLisener(item: Address)

}interface OnAddressAddesClickLisener {
    fun onAddressClickLisener( )

}
interface OnOrderClickListener {
    fun onItemsClickLisener(item: Order)

}
interface ServiceClickListener {
    fun onServiceClickLisener(item: ServiceInLaundry)

}

interface ItemsInLaundryClickListener {
    fun onItemsClickLisener(item: ItemsInService, plus: Int?)

}

interface OnAllAddressClickLisener {
    fun onRemoveAddressClickLisener(item: Address, position: Int)
    fun onDefaultAddressClickLisener(item: Address)

}