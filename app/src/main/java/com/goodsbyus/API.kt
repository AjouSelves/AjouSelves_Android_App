package com.goodsbyus

import retrofit2.Call
import retrofit2.http.*

interface API {
    // x-www-urlencoded
    // Json
    @POST("/proj/add") // Call<InitializeResponse> 데이터를 받을 data class
    fun initRequest(
        @Body initializeRequest: Posts
    ): Call<InitializeResponse> // InitializeRequest 요청을 보낼 Json Data Class

    @POST("/auth/register") //회원가입
    fun registerRequest(@Body initializeRequest: RegisterInfo): Call<InitializeResponse>

    @POST("/auth/login") //로그인
    fun loginRequest(@Body initializeRequest: LoginInfo): Call<LoginResponse>

    @GET("/proj/{id}")
    fun getRequest(
        @Path("id") userid: Int
    ): Call<List<DetailModel>>

    @GET("/proj")
    fun getList(
    ): Call<List<ItemGetModel>>

    @POST("/auth/email")
    fun checkRequest(@Body initializeRequest: MailCheck): Call<InitializeResponse>
}