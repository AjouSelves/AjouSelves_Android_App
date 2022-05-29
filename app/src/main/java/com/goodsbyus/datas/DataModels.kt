package com.goodsbyus.datas

import android.os.Parcelable
import android.provider.ContactsContract
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*



@Parcelize
class DetailResult(
    val status: String,
    val message: List<DetailModel>
): Parcelable

@Parcelize
data class DetailModel(
    var projid: Int,
    var title: String,
    var state: Int,
    var category: String,
    var min_num: Int,
    var cur_num: Int,
    var required: String,
    var explained: String,
    var created_at: String,
    var nickname: String,
    var userid: String,
    var profilelink: String,
    var photos: List<String>?
): Parcelable

@Parcelize
data class PhotoModel(
    var photos: String
): Parcelable

@Parcelize
data class ItemGetModel(
    var projid: Int,
    var title: String,
    var state: Int,
    var category: String,
    var min_num: Int,
    var cur_num: Int,
    var required: String,
    var explained: String,
    var created_at: String,
    var nickname: String,
    var userid: String,
    var profilelink: String,
    var url: String
): Parcelable

@Parcelize
data class GoodsGetModel(
    var projid: Int,
    var title: String,
    var state: Int,
    var category: String,
    var min_num: Int,
    var cur_num: Int,
    var explained: String,
    var nickname: String,
    var userid: Int,
    var profilelink: String,
    var url: String
): Parcelable

@Parcelize
data class MyFundingModel(
    var projid: Int,
    var userid: Int,
    var title: String,
    var state: Int,
    var category: String,
    var min_num: Int,
    var cur_num: Int,
    var required: String,
    var explained: String,
    var created_at: String,
    var paylink: String
): Parcelable

@Parcelize
data class MyFundingTitle(
    var projid: Int,
    var title: String
): Parcelable


data class PostModel(
    val title: String,
    val explained: String
)

data class PostResponse(
    val fieldCount: Int,
    val affectedRows: Int,
    val insertId: Int,
    val severStatus: Int,
    val warningCount: Int,
    val message: String,
    val protocol41: Boolean,
    val changeRows: Int
)

data class Posts(
    val userid: Int,
    val title: String,
    val explained: String,
    val min_num: Int,
    val category: String,
    val required: String
)

@Parcelize
data class PostList(
    val title: String,
    val explained: String,
    val created_at: String,
    val userid: Int,
    val nickname: String,
    val url: String
): Parcelable

data class InitializeResponse(
    var status: String
)

data class LoginResponse(
    var code : Int,
    @SerializedName("text")
    var message : String,
    var exp : String,
    var token : String
)

data class MailCheck(
    val email: String
)

data class FundingResponse(
    val status: String,
    val text: String
)

data class RegisterInfo(
    val email : String,
    val password : String,
    val name : String,
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

data class LoginInfo(
    val email : String,
    val password : String
)


data class StateModel(
    val state: Int
)