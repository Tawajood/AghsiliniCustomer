package com.dotjoo.aghsilinicustomer.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TermsResponse(
    @SerializedName("terms") var terms: Terms? = null,


    ) : Parcelable
@Parcelize
data class AllMessagesResponse(
    @SerializedName("messages") var messages: ArrayList<Message>? = null,


    ) : Parcelable
@Parcelize
data class AboutusResponse(
    @SerializedName("about_us") var terms: Terms? = null,


    ) : Parcelable

@Parcelize
data class Terms (
    @SerializedName("body") var body: String? = null,
      @SerializedName("id") var id: String? = null

    ) : Parcelable

@Parcelize
data class ContactResponse (
    @SerializedName("email") var email: String? = null,
      @SerializedName("phone") var phone: String? = null

    ) : Parcelable

@Parcelize
data class Message (
    @SerializedName("message") var message: String? = null,
      @SerializedName("direction") var direction: String? = null

    ) : Parcelable

@Parcelize
data class WalletResponse (
    @SerializedName("wallet") var wallet: String? = null,

    ) : Parcelable

@Parcelize
data class NotifactionResponse (
    @SerializedName("notifications") var notificationItem: ArrayList<NotificationItem>? = null
    ) : Parcelable

@Parcelize
data class NotificationItem (
    @SerializedName("id") var id: String? = null,
    @SerializedName("laundry_id") var laundry_id: String? = null,
    @SerializedName("created_at") var created_at: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("body") var body: String? = null,

) : Parcelable
