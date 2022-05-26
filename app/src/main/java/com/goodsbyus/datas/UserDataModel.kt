package com.goodsbyus.datas

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class UserRegister(
    val email : String,
    val password : String,
    val phonenumber : String,
    val nickname : String,
    val status : String,
    val socialtype : String,
    val sex : Int,
    val birth : String,
    val address : String,
    val account : String,
    val profilelink : String
)

@Parcelize
data class UserInfo(
    val email : String,
    val name : String,
    val phonenumber : String,
    val nickname : String,
    val status : String,
    val birth : String,
    val address : String,
    val account : String,
    val profilelink : String
): Parcelable

data class PayLinkModel(
    val paylink: String
)

data class PayResponse(
    val status: String,
    val text : String
)