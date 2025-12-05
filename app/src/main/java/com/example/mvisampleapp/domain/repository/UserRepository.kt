package com.example.mvisampleapp.domain.repository

import com.example.mvisampleapp.data.remote.Result
import com.example.mvisampleapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<Result<List<User>>>
}