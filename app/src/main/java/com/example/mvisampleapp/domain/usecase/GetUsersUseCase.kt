package com.example.mvisampleapp.domain.usecase

import com.example.mvisampleapp.data.repository.UserRepositoryImpl
import com.example.mvisampleapp.domain.repository.UserRepository

class GetUsersUseCase(
    private val repository: UserRepository = UserRepositoryImpl()
) {
    operator fun invoke() = repository.getUser()
}