package com.goodsbyus

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
    var photos: List<PhotoModel>?
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


data class Posts(
    val userid: Int,
    val title: String,
    val explained: String,
    val min_num: Int,
    val category: String,
    val required: String
)


data class PostList(
    val id: Int,
    val title: String,
    val url: String
)

data class InitializeResponse(
    var status: String
)

data class LoginResponse(
    var code : Int,
    var message : String,
    var token : String
)

data class MailCheck(
    val email: String
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