package com.goodsbyus.retrofit2

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

    object RetrofitBuilder {
        var api: API

        init {
            val gson=GsonBuilder()
                .setLenient()
                .create()
            val client= OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://goodsbyus.com/api/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            api = retrofit.create(API::class.java)
        }
    }