package com.example.mvisampleapp.data.repository

import com.example.mvisampleapp.data.model.UserResponse
import com.example.mvisampleapp.data.remote.ApiService
import com.example.mvisampleapp.data.remote.NetworkClient
import com.example.mvisampleapp.data.remote.Result
import com.example.mvisampleapp.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepositoryImpl : UserRepository {

    private val api: ApiService = NetworkClient.api

    override fun getUser(): Flow<Result<UserResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = api.getUser()
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(500, e.message ?: "Something went wrong"))
            }
        }.flowOn(Dispatchers.IO)
    }
}