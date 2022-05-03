package com.goodsbyus

import android.os.Parcelable
import android.provider.ContactsContract
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
class ItemGetResult(
    val status: String,
    val message: List<ItemGetModel>
): Parcelable

@Parcelize
data class ItemGetModel(
    var title: String,
    var state: Int,
    var category: String,
    var min_num: Int,
    var cur_num: Int,
    var required: String,
    var explained: String,
    var nickname: String,
    var userid: String,
    var profilelink: String,
    var photos: List<PhotoModel>?
): Parcelable

@Parcelize
data class PhotoModel(
    var photos: String
): Parcelable


data class Posts(
    val userid: Int,
    val title: String,
    val explained: Int,
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