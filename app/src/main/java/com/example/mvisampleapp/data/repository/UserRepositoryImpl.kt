package com.example.mvisampleapp.data.repository

import com.example.mvisampleapp.data.model.UserResponse
import com.example.mvisampleapp.data.remote.ApiService
import com.example.mvisampleapp.data.remote.Result
import com.example.mvisampleapp.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : UserRepository {
    override fun getUser(): Flow<Result<UserResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = apiService.getUser()
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(500, e.message ?: "Something went wrong"))
            }
        }.flowOn(Dispatchers.IO)
    }
}