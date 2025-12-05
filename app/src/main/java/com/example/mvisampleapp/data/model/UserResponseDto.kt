package com.example.mvisampleapp.data.model

import com.google.gson.annotations.SerializedName

data class UserResponseDto(
    val page: Int=0,
    @SerializedName("per_page")
    val perPage: Int=0,
    val total: Int=0,
    @SerializedName("total_pages")
    val totalPages: Int=0,
    val data: List<UserDto>
)

data class UserDto (
    val id: Long,
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("avatar")
    val avatar: String,
)
