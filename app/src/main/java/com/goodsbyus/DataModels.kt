package com.goodsbyus

import android.provider.ContactsContract
import com.google.gson.annotations.SerializedName

data class ITEM_GET_Model(
    @SerializedName("title")
    var title:String,
    @SerializedName("explained")
    var expained:String,
    @SerializedName("created_at")
    var created_at:String,
    @SerializedName("userid")
    var userid:Int,
    @SerializedName("nickname")
    var nickname: String
)

data class Posts(
    val userid: Int,
    val title: String,
    val expained: String
)

data class InitializeResponse(
    var fieldCount: Int,
    var affectedRows: Int,
    var insertId: Int,
    var serverStatus: Int,
    var warningCount: Int,
    var message: String,
    var protocol41: Boolean,
    var changedRows: Int
)