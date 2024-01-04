package com.dotjoo.aghsilinicustomer.data.webService

 import com.dotjoo.aghsilinicustomer.base.BasePagingResponse
import com.dotjoo.aghsilinicustomer.base.DevResponse
import com.dotjoo.aghsilinicustomer.base.ErrorResponse
import com.dotjoo.aghsilinicustomer.base.NetworkResponse
import com.dotjoo.aghsilinicustomer.data.Param.AddToCartParam
import com.dotjoo.aghsilinicustomer.data.Param.CreateOrderParam
import com.dotjoo.aghsilinicustomer.data.remote.response.AboutusResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.AlOrdersResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.AllAddressResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.AllLaundriesResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.AllMessagesResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.CartResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.ContactResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.CreateOrderResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.Laundry
import com.dotjoo.aghsilinicustomer.data.remote.response.LoginResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.NotifactionResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.OrderInfoResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.ReviewsResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.ServiceResponse
 import com.dotjoo.aghsilinicustomer.data.remote.response.SliderResponse
 import com.dotjoo.aghsilinicustomer.data.remote.response.TermsResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.WalletResponse
 import okhttp3.MultipartBody
 import okhttp3.RequestBody
 import retrofit2.http.*
import javax.inject.Singleton


@Singleton
interface ApiInterface {
    @POST("api/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("country_code") country_code: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): NetworkResponse<DevResponse<LoginResponse>, ErrorResponse>

    @POST("api/update/fcmToken")
    @FormUrlEncoded
    suspend fun updateFcnToken(
        @Field("device_token") device_token: String
    ): NetworkResponse<DevResponse<LoginResponse>, ErrorResponse>

    @POST("api/check/phone")
    @FormUrlEncoded
    suspend fun checkPhone(
        @Field("country_code") country_code: String, @Field("phone") phone: String
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @POST("api/send/otp")
    @FormUrlEncoded
    suspend fun checkOTpWIthPhone(
        @Field("country_code") country_code: String,
        @Field("phone") phone: String,
        @Field("otp") otp: String
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @POST("api/reset/password")
    @FormUrlEncoded
    suspend fun resetPassword(
        @Field("country_code") country_code: String,
        @Field("phone") phone: String,
        @Field("otp") otp: String,
        @Field("password") password: String
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>
  @POST("api/change/password")
    @FormUrlEncoded
    suspend fun changePass (
           @Field("old_password") old_password: String,
           @Field("password") password: String,
      @Field("password_confirmation") password_confirmation: String?
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>
@POST("api/change/phone")
    @FormUrlEncoded
    suspend fun changrPhone (
           @Field("country_code") country_code: String,
           @Field("phone") phone: String,
     ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @POST("api/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("name") name: String,
        @Field("country_code") country_code: String,
        @Field("phone") phone: String,
        @Field("lat") lat: String,
        @Field("lon") lon: String,
        @Field("address") address: String?,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String?
    ): NetworkResponse<DevResponse<LoginResponse>, ErrorResponse>


    @POST("api/logout")
     suspend fun logout(): NetworkResponse<DevResponse<Any>, ErrorResponse>
 @GET("api/get/sliders")
     suspend fun getSlider(): NetworkResponse<DevResponse<SliderResponse>, ErrorResponse>

    @GET("api/get/all/laundries/")
    suspend fun getAllLaundries(
        @Query("page") page: Int,
        @Query("lat") lat: String,
        @Query("lon") lon: String,
    ): NetworkResponse<BasePagingResponse<Laundry>, ErrorResponse>

    @FormUrlEncoded
    @POST("api/laundries/search")
    suspend fun searchAllLaundries(
        @Query("page") page: Int,
     @Field("name") name: String,
    ): NetworkResponse<BasePagingResponse<Laundry>, ErrorResponse>


    @GET("api/get/nearst/laundries")
    suspend fun getNearestLaundries(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
    ): NetworkResponse<DevResponse<AllLaundriesResponse>, ErrorResponse>

    @GET("api/get/address")
    suspend fun getAllAddresses(
    ): NetworkResponse<DevResponse<AllAddressResponse>, ErrorResponse>

    @POST("api/create/order")
    suspend fun createOrder(
        @Body param: CreateOrderParam
    ): NetworkResponse<DevResponse<CreateOrderResponse>, ErrorResponse>

    @POST("api/adress/store")
    @FormUrlEncoded
    suspend fun addAddress(
        @Field("lat") lat: String,
        @Field("lon") lon: String,
        @Field("address") address: String?,
    ): NetworkResponse<DevResponse<AllAddressResponse>, ErrorResponse>
    @FormUrlEncoded
    @POST("api/change/current/address")
    suspend fun changeCurrentAddress(
        @Field("id") id: String,
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @GET("api/get/curent/order")
    suspend fun getCurrentOrder(
    ): NetworkResponse<DevResponse<AlOrdersResponse>, ErrorResponse>

    @GET("api/get/new/order")
    suspend fun getNewOrder(
    ): NetworkResponse<DevResponse<AlOrdersResponse>, ErrorResponse>

    @GET("api/get/previous/order")
    suspend fun getPrevOrder(
    ): NetworkResponse<DevResponse<AlOrdersResponse>, ErrorResponse>

    @GET("api/get/order")
    suspend fun getOrderInfo(
        @Query("order_id") order_id: String,

        ): NetworkResponse<DevResponse<OrderInfoResponse>, ErrorResponse>

    @GET("api/get/cart")
    suspend fun getCart(
    ): NetworkResponse<DevResponse<CartResponse>, ErrorResponse>

    @POST("api/add/cart")
    suspend fun addToCart(
        @Body param: AddToCartParam
    ): NetworkResponse<DevResponse<CartResponse>, ErrorResponse>

    @GET("general/get/services")
    suspend fun getServices(
    ): NetworkResponse<DevResponse<ServiceResponse>, ErrorResponse>

    @GET("api/increase/item")
    suspend fun increaseItem(
        @Query("item_id") item_id: String,
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @GET("api/decrease/item")
    suspend fun decreaseItem(
        @Query("item_id") item_id: String,
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @GET("api/get/items")
    suspend fun getItemsInService(
        @Query("laundry_id") laundry_id: String,
    ): NetworkResponse<DevResponse<ServiceResponse>, ErrorResponse>

    @GET("api/remove/item")
    suspend fun removeItem(
        @Query("item_id") item_id: String,
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @GET("general/terms&condation")
    suspend fun getTerms_condition(
    ): NetworkResponse<DevResponse<TermsResponse>, ErrorResponse>

    @GET("general/aboutus")
    suspend fun getAbout(
    ): NetworkResponse<DevResponse<AboutusResponse>, ErrorResponse>

    @GET("general/contact")
    suspend fun getContact(
    ): NetworkResponse<DevResponse<ContactResponse>, ErrorResponse>

    @GET("general/get/messages/?type=customer")
    suspend fun getMessages(
    ): NetworkResponse<DevResponse<AllMessagesResponse>, ErrorResponse>

    @FormUrlEncoded
    @POST("general/send/message")
    suspend fun sendMessage(
        @Field("message") message: String,
        @Field("type") type: String = "customer",
        ): NetworkResponse<DevResponse<AllMessagesResponse>, ErrorResponse>

    @POST("api/charge/wallet")
    @FormUrlEncoded
    suspend fun chargeWallet(
        @Field("balance") balance: String,
        @Field("fort_id") fort_id: String,
        ): NetworkResponse<DevResponse<Any>, ErrorResponse>


    @GET("api/get/wallet")
     suspend fun getWallet(
        ): NetworkResponse<DevResponse<WalletResponse>, ErrorResponse>


    @GET("api/get/notifications")
     suspend fun getNotifaction(
        ): NetworkResponse<DevResponse<NotifactionResponse>, ErrorResponse>


    @GET("api/delete/account")
     suspend fun deleteAccount(
        ): NetworkResponse<DevResponse<Any>, ErrorResponse>

 @POST("api/review/laundry")
 @FormUrlEncoded
     suspend fun reviewLaundry(
     @Field("order_id")     order_id: String,
     @Field("rate")        rate: String,
     @Field("note")        note: String,
     @Field("laundry_id")  laundry_id: String,
 ): NetworkResponse<DevResponse<Any>, ErrorResponse>

 @GET("api/laundry/reviews")
      suspend fun getLaundryReview(
@Query("laundry_id")  laundry_id: String,
 ): NetworkResponse<DevResponse<ReviewsResponse>, ErrorResponse>

    @FormUrlEncoded
@POST("api/make/complaint")
     suspend fun complainLaundry(
     @Field("order_id") order_id: String,
      @Field("note") note: String,
     @Field("laundry_id") laundry_id: String,
 ): NetworkResponse<DevResponse<Any>, ErrorResponse>

 @GET("api/cancel/order")
     suspend fun cancelOrder(
     @Query("order_id") order_id: String,

 ): NetworkResponse<DevResponse<Any>, ErrorResponse>

 @GET("api/delete/address")
     suspend fun deleteAddress(
     @Query("id") id: String,

 ): NetworkResponse<DevResponse<Any>, ErrorResponse>


 @GET("api/get/profile")
     suspend fun getProfile(
 ): NetworkResponse<DevResponse<LoginResponse>, ErrorResponse>


 @POST("api/update/profile")
 @Multipart
 @JvmSuppressWildcards
 suspend fun updateProfile(
     @PartMap updateMap: Map<String, RequestBody>,
     @Part image: MultipartBody.Part?): NetworkResponse<DevResponse<Any>, ErrorResponse>

  @GET("api/update/lang")
     suspend fun updatlang(
 ): NetworkResponse<DevResponse<Any>, ErrorResponse>


}