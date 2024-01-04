package com.dotjoo.aghsilinicustomer.data

import com.dotjoo.aghsilinicustomer.base.AllLaundriesParams
import com.dotjoo.aghsilinicustomer.data.Param.AddAddressParam
import com.dotjoo.aghsilinicustomer.data.Param.AddToCartParam
import com.dotjoo.aghsilinicustomer.data.Param.ChangeCurrentAddressParam
import com.dotjoo.aghsilinicustomer.data.Param.ChangePasswordParams
import com.dotjoo.aghsilinicustomer.data.Param.ChargeWalletParam
import com.dotjoo.aghsilinicustomer.data.Param.CheckOtpWithPhoneParam
import com.dotjoo.aghsilinicustomer.data.Param.CheckPhoneParam
import com.dotjoo.aghsilinicustomer.data.Param.CompalinParam
import com.dotjoo.aghsilinicustomer.data.Param.CreateOrderParam
import com.dotjoo.aghsilinicustomer.data.Param.DeleteAddressParam
import com.dotjoo.aghsilinicustomer.data.Param.GetItemsInServiceParam
import com.dotjoo.aghsilinicustomer.data.Param.IncreaseItemParam
import com.dotjoo.aghsilinicustomer.data.Param.LaundryReviewParam
import com.dotjoo.aghsilinicustomer.data.Param.LoginParams
import com.dotjoo.aghsilinicustomer.data.Param.OrderInfoParam
import com.dotjoo.aghsilinicustomer.data.Param.RegisterParams
import com.dotjoo.aghsilinicustomer.data.Param.RemoveItemParam
import com.dotjoo.aghsilinicustomer.data.Param.ResetPasswordParams
import com.dotjoo.aghsilinicustomer.data.Param.ReviewParam
import com.dotjoo.aghsilinicustomer.data.Param.SendMessageParam
import com.dotjoo.aghsilinicustomer.data.Param.UpdateFcmTokenParam
import com.dotjoo.aghsilinicustomer.data.Param.UpdatePhoneParam
import com.dotjoo.aghsilinicustomer.data.Param.UpdateProfileParam
import com.dotjoo.aghsilinicustomer.data.Param.toMap
import com.dotjoo.aghsilinicustomer.data.webService.ApiInterface
import com.dotjoo.aghsilinicustomer.util.FileManager.toMultiPart
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import javax.inject.Inject


class Repository @Inject constructor(private val api: ApiInterface) {
    suspend fun login(param: LoginParams) = api.login(param.countryCode, param.phone, param.pass)
    suspend fun register(param: RegisterParams) = api.register(
        param.name,
        param.country_code,
        param.phone,
        param.lat,
        param.lon,
        param.address,
        param.lat,
        param.lat
    )

    suspend fun logout() = api.logout()
    suspend fun getSlider() = api.getSlider()

    suspend fun resetpassword(param: ResetPasswordParams) =
        api.resetPassword(param.countryCode, param.phone, param.otp, param.pass)
 suspend fun changePass(param: ChangePasswordParams) =
        api.changePass(param.old_password, param.password, param.password_confirmation )

    suspend fun checkPhone(param: CheckPhoneParam) = api.checkPhone(param.countryCode, param.phone)
    suspend fun checkOTpWIthPhone(param: CheckOtpWithPhoneParam) =
        api.checkOTpWIthPhone(param.countryCode, param.phone, param.otp)

    suspend fun updateFcmToken(params: UpdateFcmTokenParam) = api.updateFcnToken(params.fcm_token)

    suspend fun getAllLaundries(page: Int, lat: String, lng: String) =
        api.getAllLaundries(page,  lat, lng)
suspend fun searchAllLaundries(page: Int , name:String ) =
        api.searchAllLaundries(page , name)

    suspend fun getNearestLaundries(param: AllLaundriesParams) =
         api.getNearestLaundries(param. lat, param.lng)
            //param.lat   ,param.lng)
            //"30.256456", "31.546155615")

    suspend fun getAllAddresses() = api.getAllAddresses()
    suspend fun createOrder( param: CreateOrderParam  ) = api.createOrder(param)
    suspend fun addAddress( param: AddAddressParam  ) = api.addAddress(param.lat,param.lng,param.address)
    suspend fun changeCurrentAddress( param: ChangeCurrentAddressParam) = api.changeCurrentAddress(param.id)
    suspend fun getCurrentOrder() = api.getCurrentOrder()
    suspend fun getPrevOrder() = api.getPrevOrder()
    suspend fun getNewOrder() = api.getNewOrder()
    suspend fun getCart() = api.getCart()
    suspend fun addToCart(param: AddToCartParam) = api.addToCart(param)
    suspend fun increaseItem(param: IncreaseItemParam) = api.increaseItem(param.item_id)
    suspend fun decreaseItem(param: IncreaseItemParam) = api.decreaseItem(param.item_id)
    suspend fun removeItem(param: RemoveItemParam) = api.removeItem(param.item_id)
    suspend fun getItemsInService(param: GetItemsInServiceParam) = api.getItemsInService(param.laundry_id  )
    suspend fun getOrderInfo(param:OrderInfoParam) = api.getOrderInfo(param.orderID)

    suspend fun getTerms_condition() = api.getTerms_condition()
    suspend fun getAbout() = api.getAbout()
    suspend fun getContact() = api.getContact()
     suspend fun getMessages() = api.getMessages()
    suspend fun getNotifaction() = api.getNotifaction()
   suspend fun getWallet() = api.getWallet()
    suspend fun sendMessage(param: SendMessageParam) = api.sendMessage(param.msg)
    suspend fun chargeWallet(param: ChargeWalletParam) = api.chargeWallet(param.balance, param.fort_id)

    suspend fun deleteAccount() = api.deleteAccount()
    suspend fun reviewLaundry(param: ReviewParam) = api.reviewLaundry(param.order_id,param.rate,param.note,param.laundry_id,)
    suspend fun getLaundryReview(param: LaundryReviewParam) = api.getLaundryReview( param.laundry_id,)
    suspend fun complainLaundry(param: CompalinParam) = api.complainLaundry(param.order_id,param.note,param.laundry_id,)
    suspend fun cancelOrder(param: OrderInfoParam) = api.cancelOrder(param.orderID)
    suspend fun deleteAddress(param: DeleteAddressParam) = api.deleteAddress(param.id)
    suspend fun getProfile( ) = api.getProfile( )
    suspend fun updatlang( ) = api.updatlang( )
    suspend fun updateProfile(param: UpdateProfileParam) = api.updateProfile( param.toMap(),
        param.img?.toMultiPart("img"),
    )
    suspend fun changrPhone(param: UpdatePhoneParam) = api.changrPhone(
        param.country_code,
        param.phone,)

}