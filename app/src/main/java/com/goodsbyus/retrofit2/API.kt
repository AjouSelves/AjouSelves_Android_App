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

    @POST("post") // Call<InitializeResponse> 데이터를 받을 data class
    fun postRequest(
        @Body initializeRequest: PostModel
    ): Call<PostResponse> // InitializeRequest 요청을 보낼 Json Data Class

    @POST("proj") // Call<InitializeResponse> 데이터를 받을 data class
    fun initRequest(
        @Body initializeRequest: Posts
    ): Call<InitializeResponse> // InitializeRequest 요청을 보낼 Json Data Class

    @Multipart
    @POST("proj/photo") // Call<InitializeResponse> 데이터를 받을 data class
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
    @POST("proj/pay/qr/{id}") //qr 등록
    fun postPayLink(
        @Path("id") projid: Int,
        @Part ("paylink") paylink: RequestBody,
        @Part photo : MultipartBody.Part?
    ): Call<PayResponse>

    @POST("comment")
    fun addComment(
        @Body initializeRequest: PostComment
    ): Call<InitializeResponse>

    @GET("post") //커뮤니티 게시글 불러오기
    fun getPost(
    ): Call<PostGetModel>

    @GET("proj/{id}") //굿즈 상세정보
    fun getRequest(
        @Path("id") projid: Int
    ): Call<List<DetailModel>>

    @GET("post/{id}")
    fun getPostInfo(
        @Path("id") postid: Int
    ): Call<PostDetailModel>

    @GET("proj/join/{id}") //펀딩하기
    fun getFunding(
        @Path("id") projid: Int
    ): Call<FundingResponse>

    @GET("user/join-detail") //내가 펀딩한 굿즈 목록
    fun getMyFunding(
    ): Call<MyGoods>

    @GET("user/join")
    fun getMyFundingTitle(
    ): Call<List<MyFundingTitle>>

    @GET("user/create")
    fun getMyGoodsTitle(
    ): Call<List<MyFundingTitle>>

    @GET("user/create-detail") //내가 생성한 굿즈 목록
    fun getMyGoods(
    ): Call<MyGoods>

    @GET("proj") //모든 굿즈 프로젝트 불러오기
    fun getList(
    ): Call<List<ItemGetModel>>

    @GET("user") //유저 상세 정보
    fun getUserInfo(
    ): Call<UserInfo>

    @GET("proj/pay/qr/{id}") //qr 링크 불러오기
    fun getPayLink(
        @Path("id") projid: Int
    ): Call<GetPay>

    @GET("proj/leave/{id}")
    fun getLeaveJoin(
        @Path("id") projid: Int
    ): Call<FundingResponse>

    @POST("auth/email") //email 인증
    fun checkRequest(@Body initializeRequest: MailCheck): Call<EmailResponse>

    @DELETE("proj/{id}") //프로젝트 삭제
    fun deleteProject(
        @Path("id") projid: Int
    ): Call<FundingResponse>

    @DELETE("user")
    fun deleteUser(): Call<FundingResponse>

    @PUT("state/{id}") //프로젝트 상태 변경
    fun putState(
        @Path("id") projid: Int,
        @Body initializeRequest: StateModel
    ): Call<PayResponse>

    @PUT("user")
    fun putUserInfo(
        @Body initializeRequest: UserPutData
    ): Call<UserResponse>


}