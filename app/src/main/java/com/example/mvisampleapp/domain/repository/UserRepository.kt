package com.example.mvisampleapp.domain.repository

import com.example.mvisampleapp.data.model.UserResponse
import com.example.mvisampleapp.data.remote.Result
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<Result<UserResponse>>
}