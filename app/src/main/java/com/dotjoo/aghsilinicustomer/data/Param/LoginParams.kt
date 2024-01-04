package com.dotjoo.aghsilinicustomer.data.Param

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class LoginParams(val countryCode :String="",val phone :String="",val pass :String="" )

data class RegisterParams(

  val name: String,
  val country_code: String,
  val phone: String,
  val lat: String,
  val lon: String,
  val address: String?,
  val password: String,
  val password_confirmation: String? )

data class UpdateProfileParam(

    val name: String,
    val country_code: String,
    val phone: String,
    val img: File?,
   )

fun UpdateProfileParam.toMap(): Map<String, RequestBody>{
    val itemMap = hashMapOf<String, RequestBody>()
    itemMap["name"] = name.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    itemMap["country_code"] = country_code.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    itemMap["phone"] = phone.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    return itemMap
}
data class UpdatePhoneParam(

   val country_code: String,
  val phone: String,
   )

@Parcelize
data class AddAddressParams(
  var lat: String?,
  var long: String?,
  var  city: String?,var district: String?, var street: String?, var building: String?,var floor: String?,var flat: String? , var fullAddress:String?
) : Parcelable


data class ResetPasswordParams(val countryCode :String="",val phone :String="",val pass :String="" ,val otp :String="" )
data class ChangePasswordParams( val old_password: String,val password: String,
                                 val password_confirmation: String? )
data class CheckPhoneParam(val countryCode :String="",val phone :String="" )
data class CheckOtpWithPhoneParam(val countryCode :String="",val phone :String=""  ,val otp :String="" )
