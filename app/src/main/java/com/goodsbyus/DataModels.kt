package com.goodsbyus

import android.provider.ContactsContract
import com.google.gson.annotations.SerializedName
import java.util.*

data class ITEM_GET_Model(
    var projid: Int,
    var userid: Int,
    var title: String,
    var state: Int,
    var category: String,
    var min_num: Int,
    var required: String,
    var explained: Int,
    var paylink: String?,
    var email: String,
    var password: String,
    var salt: String,
    var phonenumber: String,
    var nickname: String,
    var status: String,
    var socialtype: String,
    var sex: Int,
    var birth: String,
    var address: String,
    var account: String,
    var create_at: String,
    var profilelink: String,
)

data class Posts(
    val userid: Int,
    val title: String,
    val explained: Int,
    val min_num: Int,
    val category: String,
    val required: String
)

data class InitializeResponse(
    var status: String
)