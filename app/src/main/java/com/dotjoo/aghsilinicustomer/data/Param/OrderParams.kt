package com.dotjoo.aghsilinicustomer.data.Param

import com.google.gson.annotations.SerializedName


data class UpdateFcmTokenParam(val fcm_token :String="" )
data class CreateOrderParam( var lat         : String?  = null,
                             var lon         : String?     = null,
                             var address     : String?  = null,
                             var laundry_id   : String?     = null,
                             var total       : String?     = null,
                             var argent      : Boolean? = null,
                             var tax         : String?     = null,
                             var delivery    : String?     = null,
                             var payment_type : Int?     = null
)
data class OrderInfoParam(val orderID :String="" )
data class IncreaseItemParam(val item_id :String="", val plus :Boolean )
data class RemoveItemParam(val item_id :String=""  )
data class GetItemsInServiceParam(val laundry_id :String="", val service_id :String="" )
data class AddAddressParam(val lat :String="", val lng :String="" , val address :String="" )
data class ChangeCurrentAddressParam(val  id :String=""  )

data class AddToCartParam(@SerializedName("laundry_id" ) var laundry_id   : String?     = null,
                          @SerializedName("urgent" ) var argent       : Boolean?     = null, @SerializedName("items" ) var items : ArrayList<Items> = arrayListOf()
)

data class Items (

    @SerializedName("item_id" ) var itemId : String? = null,
    @SerializedName("count"   ) var count  : Int? = null,
    @SerializedName("price"   ) var price  : String? = null

)
data class ReviewParam (
  var  order_id: String,
  var  rate: String,
  var  note: String,
  var  laundry_id: String,

)
data class LaundryReviewParam (

  var  laundry_id: String,

)
data class CompalinParam (
  var  order_id: String,
   var  note: String,
  var  laundry_id: String,

)
data class DeleteAddressParam(val id :String="" )
