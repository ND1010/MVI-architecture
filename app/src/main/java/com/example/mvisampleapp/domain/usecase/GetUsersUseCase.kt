package com.example.mvisampleapp.domain.usecase

import com.example.mvisampleapp.data.model.UserResponse
import com.example.mvisampleapp.data.remote.Result
import com.example.mvisampleapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<Result<UserResponse>> = repository.getUser()
}