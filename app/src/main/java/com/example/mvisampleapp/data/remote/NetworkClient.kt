package com.example.mvisampleapp.data.remote

import com.example.mvisampleapp.data.remote.ApiEndpoints.HOST_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val TIMEOUT_TIME = 30000L

object NetworkClient {

    val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(provideLoggingInterceptor())
            .readTimeout(TIMEOUT_TIME, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_TIME, TimeUnit.SECONDS)
            .callTimeout(TIMEOUT_TIME, TimeUnit.SECONDS)
            .build()
    }

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(HOST_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

}