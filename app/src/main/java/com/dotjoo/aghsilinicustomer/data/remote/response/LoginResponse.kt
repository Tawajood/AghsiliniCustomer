package com.dotjoo.aghsilinicustomer.data.remote.response

import android.os.Parcelable
import android.service.autofill.UserData
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponse(
    @SerializedName("token") var token: String? = null,
    @SerializedName("user") var user: UserResponse? = null,

    ) : Parcelable

@Parcelize
data class UserResponse(
     @SerializedName("name") var name: String? = null,
     @SerializedName("country_code") var country_code: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("img") var img: String? = null,



    ) : Parcelable
