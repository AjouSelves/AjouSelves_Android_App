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
    var photos: List<String>?,
    var amount: Int,
    var is_poster: Int,
    var is_joined: Int
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
    val amount: Int,
    var userid: String,
    var profilelink: String,
    var url: String
): Parcelable


data class MyGoods(
    val status: String,
    val data: List<GoodsGetModel>
)

@Parcelize
data class GoodsGetModel(
    var projid: Int,
    var title: String,
    var state: Int,
    var category: String,
    var min_num: Int,
    var cur_num: Int,
    var created_at: String,
    var explained: String,
    var nickname: String,
    var userid: Int,
    var amount: Int,
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

data class PostGetModel(
    val status: String,
    val data: List<PostList>
)

@Parcelize
data class PostList(
    val postid: Int,
    val title: String,
    val explained: String,
    val created_at: String,
    val userid: Int,
    val nickname: String,
    val url: String
): Parcelable

data class PostDetailModel(
    val status: String,
    @SerializedName("post")
    val data: List<PostDetail>
)

@Parcelize
data class PostDetail(
    val postid: Int,
    val nickname: String?,
    val title: String?,
    val explained: String?,
    val created_at: String,
    val photos: List<String>?,
    val userid: Int?,
    val comments: String?
): Parcelable

data class PostComment(
    val postid: Int,
    val comment: String
)

data class InitializeResponse(
    var status: String
)

data class EmailResponse(
    val number: Int,
    val status: String,
    val text: String,
    val error: String
)

data class LoginResponse(
    var status: String,
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

data class GetPay(
    val status: String,
    val text: String,
    val paylink: String,
    val qr_url: String
)