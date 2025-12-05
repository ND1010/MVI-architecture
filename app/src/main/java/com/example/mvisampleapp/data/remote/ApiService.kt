package com.example.mvisampleapp.data.remote

import com.example.mvisampleapp.data.model.UserResponseDto
import retrofit2.http.GET

interface ApiService {

    @GET(ApiEndpoints.USER)
    suspend fun getUser(): UserResponseDto

}