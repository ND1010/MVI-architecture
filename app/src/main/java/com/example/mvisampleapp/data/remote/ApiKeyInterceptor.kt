package com.example.mvisampleapp.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("x-api-key", "reqres_2af5725cba36400e94c590761c0cac64")
            .build()
        return chain.proceed(request)
    }
}