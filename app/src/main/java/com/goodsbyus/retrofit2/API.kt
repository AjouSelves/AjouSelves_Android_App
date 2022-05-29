package com.goodsbyus.retrofit2

import retrofit2.Call
import retrofit2.http.*
import com.goodsbyus.datas.*
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody

interface API {
    // x-www-urlencoded
    // Json

    @POST("post/add") // Call<InitializeResponse> 데이터를 받을 data class
    fun postRequest(
        @Body initializeRequest: PostModel
    ): Call<PostResponse> // InitializeRequest 요청을 보낼 Json Data Class

    @POST("proj/add") // Call<InitializeResponse> 데이터를 받을 data class
    fun initRequest(
        @Body initializeRequest: Posts
    ): Call<InitializeResponse> // InitializeRequest 요청을 보낼 Json Data Class

    @Multipart
    @POST("proj/add_photo") // Call<InitializeResponse> 데이터를 받을 data class
    fun initPicRequest(
        @Part photo : MultipartBody.Part?,
        @Part ("userid") userid: RequestBody,
        @Part ("title") title: RequestBody,
        @Part ("explained") explained: RequestBody,
        @Part ("category") category: RequestBody,
        @Part ("min_num") minnum: RequestBody
    ): Call<InitializeResponse>

    @POST("auth/register") //회원가입
    fun registerRequest(@Body initializeRequest: RegisterInfo): Call<InitializeResponse>

    @POST("auth/login") //로그인
    fun loginRequest(@Body initializeRequest: LoginInfo): Call<LoginResponse>

    @Multipart
    @POST("proj/pay/qr/add/{id}")
    fun postPayLink(
        @Path("id") projid: Int,
        @Part ("paylink") paylink: RequestBody,
        @Part photo : MultipartBody.Part?
    ): Call<PayResponse>

    @GET("post/all")
    fun getPost(
    ): Call<List<PostList>>

    @GET("proj/{id}")
    fun getRequest(
        @Path("id") projid: Int
    ): Call<List<DetailModel>>

    @GET("proj/join/{id}")
    fun getFunding(
        @Path("id") projid: Int
    ): Call<FundingResponse>

    @GET("user/join-detail")
    fun getMyFunding(
    ): Call<List<GoodsGetModel>>

    @GET("user/join")
    fun getMyFundingTitle(
    ): Call<List<MyFundingTitle>>

    @GET("user/create")
    fun getMyGoodsTitle(
    ): Call<List<MyFundingTitle>>

    @GET("user/create-detail")
    fun getMyGoods(
    ): Call<List<GoodsGetModel>>

    @GET("proj")
    fun getList(
    ): Call<List<ItemGetModel>>

    @GET("user")
    fun getUserInfo(
    ): Call<List<UserInfo>>

    @POST("auth/email")
    fun checkRequest(@Body initializeRequest: MailCheck): Call<InitializeResponse>

    @DELETE("proj/delete/{id}")
    fun deleteProject(
        @Path("id") projid: Int
    ): Call<FundingResponse>

    @PUT("proj/edit_state/{id}")
    fun putState(
        @Path("id") projid: Int,
        @Body initializeRequest: StateModel
    ): Call<PayResponse>


}