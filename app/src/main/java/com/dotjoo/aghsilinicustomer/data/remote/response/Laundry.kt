package com.dotjoo.aghsilinicustomer.data.remote.response

import android.os.Parcelable
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.io.Serializable

@Parcelize
data class AllLaundriesResponse(
    @SerializedName("laundries") var laundries: ArrayList<Laundry>? = arrayListOf(),

    ) : Parcelable

@Parcelize
data class Laundry(
    @SerializedName("id") var id: String? = null,
    @SerializedName("distance") var distance: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("lat") var lat: String? = null,
    @SerializedName("lon") var lon: String? = null,
    @SerializedName("logo") var logo: String? = null,
    @SerializedName("rate") var rate: String? = null,
    @SerializedName("phone") var phone: String? = null,
    var count: Int = 0
) : Parcelable

@Parcelize
data class AlOrdersResponse(
    @SerializedName("orders") var orders: kotlin.collections.ArrayList<Order> = arrayListOf(),

    ) : Parcelable


data class Order(
    @SerializedName("id") var id: String? = null,
    @SerializedName("laundry") var laundry: String? = null,
    @SerializedName("created_at") var created_at: String? = null,
    @SerializedName("items_count") var items_count: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("argent") var argent: Int? = null,

    @SerializedName("logo") var logo: String? = null,

    ) : Serializable

@Parcelize

data class SliderHome(
    @SerializedName("id") var id: String? = null,
    @SerializedName("img") var img: String? = null,
) : Parcelable


@Parcelize
data class SliderResponse(
    @SerializedName("sliders") var sliders: @RawValue ArrayList<SliderHome>? = arrayListOf(),

    ) : Parcelable

@Parcelize
data class AllAddressResponse(
    @SerializedName("address") var address: @RawValue ArrayList<Address>? = arrayListOf(),

    ) : Parcelable

@Parcelize
data class Address(

    @SerializedName("id") var id: String? = null,
    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("lon") var lon: String? = null,
    @SerializedName("lat") var lat: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("current") var current: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    var choosen: Boolean? = false

) : Parcelable

@Parcelize
data class OrderInfoResponse(

    @SerializedName("order") var order: OrderInfo? = OrderInfo(),
    @SerializedName("total_items_price") var totalItemsPrice: String? = null

) : Parcelable

@Parcelize
data class CartResponse(

    @SerializedName("cartitems") var cartitems: ArrayList<Cartitems> = arrayListOf(),
    @SerializedName("total_items_price") var totalItemsPrice: String? = null,
    @SerializedName("tax") var tax: String? = null,
    @SerializedName("delivery") var delivery: String? = null,
    @SerializedName("total") var total: String? = null,
    @SerializedName("urgent") var urgent: Int? = null,
    @SerializedName("laundry_id") var laundry_id: String? = null,
    @SerializedName("laundry") var laundry: Laundry? = null,

    ) : Parcelable

@Parcelize
data class Cartitems(

    @SerializedName("item_id") var itemId: String? = null,
    @SerializedName("service_name") var service_name: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("count") var count: Int = 0

) : Parcelable

@Parcelize
data class ServiceResponse(

    @SerializedName("services") var services: ArrayList<ServiceInLaundry> = arrayListOf(),

    ) : Parcelable


@Parcelize
data class ServiceInLaundry(

    @SerializedName("id") var itemId: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("items") var items: ArrayList<ItemsInService> = arrayListOf(),
    var choosen: Boolean? = false

) : Parcelable

@Parcelize
data class ItemsInService(


    @SerializedName("id") var id: String? = null,
    @SerializedName("laundry_id") var laundryId: String? = null,
    @SerializedName("service_id") var serviceId: String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("argent_price") var argentPrice: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("name") var name: String? = null,
    var count: Int? = 0

) : Parcelable

@Parcelize
data class OrderInfo(
    @SerializedName("id") var id: String? = null,
    @SerializedName("argent") var argent: Int? = null,
    @SerializedName("tax") var tax: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("delivery") var delivery: String? = null,
    @SerializedName("total") var total: String? = null,
    @SerializedName("additional_cost") var additional_cost: String? = null,
    @SerializedName("customer_name") var customerName: String? = null,
    @SerializedName("review") var review: Boolean? = null,
    @SerializedName("cancelation") var cancelation: Boolean? = null,
    @SerializedName("progress") var progress: String? = null,
    @SerializedName("payment_type") var payment_type: Int? = null,
    @SerializedName("lat") var lat: String? = null,
    @SerializedName("lon") var lon: String? = null,
    @SerializedName("laundry_id") var laundryId: String? = null,
    @SerializedName("driver_id") var driverId: String? = null,
    @SerializedName("orderitems") var orderitems: ArrayList<OrderInfoItem> = arrayListOf(),
    @SerializedName("laundry") var laundry: Laundry? = Laundry(),
    @SerializedName("driver") var driver: Driver? = null
) : Parcelable

@Parcelize
data class Driver(
    @SerializedName("id") var id: String? = null,
    @SerializedName("country_code") var country_code: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("driveing_licence") var driveing_licence: String? = null,
    @SerializedName("car_form") var car_form: String? = null,
    @SerializedName("national_id") var national_id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("img") var img: String? = null,
    @SerializedName("updated_at") var updated_at: String? = null

) : Parcelable

@Parcelize
data class OrderInfoItem(


    @SerializedName("id") var id: Int? = null,
    @SerializedName("order_id") var orderId: Int? = null,
    @SerializedName("item_id") var itemId: Int? = null,
    @SerializedName("count") var count: Int? = null,
    @SerializedName("price") var price: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("item") var item: Item? = Item()

) : Parcelable

@Parcelize
data class Item(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("laundry_id") var laundryId: Int? = null,
    @SerializedName("service_id") var serviceId: Int? = null,
    @SerializedName("price") var price: Int? = null,
    @SerializedName("argent_price") var argentPrice: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("service") var service: ServiceInLaundry? = null,

    ) : Parcelable

@Parcelize
data class CreateOrderResponse(
    @SerializedName("order") var order: Order? = null,

    ) : Parcelable

@Parcelize
data class ReviewsResponse(

    @SerializedName("reviews") var reviews: @RawValue ArrayList<ReviewItem> = arrayListOf(),

    ) : Parcelable

@Parcelize
data class ReviewItem(


    @SerializedName("id") var id: Int? = null,
    @SerializedName("laundry_id") var laundryId: Int? = null,
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("order_id") var orderId: Int? = null,
    @SerializedName("rate") var rate: Int? = null,
    @SerializedName("note") var note: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("user") var user: UserResponse? = UserResponse()

) : Parcelable
