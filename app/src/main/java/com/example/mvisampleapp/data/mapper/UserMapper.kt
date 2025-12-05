package com.example.mvisampleapp.data.mapper

import com.example.mvisampleapp.data.model.UserDto
import com.example.mvisampleapp.data.model.UserResponseDto
import com.example.mvisampleapp.domain.model.User

fun UserDto.toDomain(): User =
    User(
        id = id,
        email = email,
        name = "$firstName $lastName",
        avatar = avatar
    )

fun UserResponseDto.toDomainList(): List<User> =
    data.map { it.toDomain() }