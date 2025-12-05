package com.example.mvisampleapp.data.repository

import com.example.mvisampleapp.data.mapper.toDomainList
import com.example.mvisampleapp.data.remote.ApiService
import com.example.mvisampleapp.data.remote.NetworkChecker
import com.example.mvisampleapp.data.remote.Result
import com.example.mvisampleapp.domain.model.User
import com.example.mvisampleapp.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val networkChecker: NetworkChecker
) : UserRepository {
    override fun getUser(): Flow<Result<List<User>>> {
        return flow {
            emit(Result.Loading)

            if (networkChecker.isNetworkAvailable().not()) {
                emit(Result.Error(-1, "No internet connection"))
                return@flow
            }

            try {
                val response = apiService.getUser()
                val mappedUser = response.toDomainList()
                emit(Result.Success(mappedUser))
            } catch (e: Exception) {
                emit(Result.Error(500, e.message ?: "Something went wrong"))
            }
        }.flowOn(Dispatchers.IO)
    }
}