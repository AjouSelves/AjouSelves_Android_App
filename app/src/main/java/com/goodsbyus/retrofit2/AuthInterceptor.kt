package com.goodsbyus.retrofit2

import com.goodsbyus.GlobalApplication
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override  fun intercept(chain: Interceptor.Chain): Response {
        val request=chain.request().newBuilder()
            .addHeader("Authorization", getToken()?:"")
            .build()

        return chain.proceed(request)
    }

    private fun getToken(): String {
        return GlobalApplication.prefs.getString("tokens", "xxxxxx")
    }
}