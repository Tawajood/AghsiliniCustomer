package com.dotjoo.aghsilinicustomer.data.Param

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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
