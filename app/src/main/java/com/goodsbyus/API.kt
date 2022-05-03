package com.goodsbyus

import retrofit2.Call
import retrofit2.http.*

interface API {
    // x-www-urlencoded
    // Json
    @POST("/proj/add") // Call<InitializeResponse> 데이터를 받을 data class
    fun initRequest(@Body initializeRequest: Posts): Call<InitializeResponse> // InitializeRequest 요청을 보낼 Json Data Class

    @GET("/proj/{id}")
    fun getRequest(
        @Path("id") userid: Int
    ): Call<List<ItemGetModel>>

    @GET("/proj")
    fun getList(
    ): Call<ItemGetResult>
}